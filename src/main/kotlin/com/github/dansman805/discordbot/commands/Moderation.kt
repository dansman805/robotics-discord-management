package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.dataclasses.MembershipTimeRole
import com.github.dansman805.discordbot.editDeleteChannelID
import com.github.dansman805.discordbot.memberShipRoles
import com.github.dansman805.discordbot.modLogChannelID
import com.google.common.eventbus.Subscribe
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.annotation.Precondition
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.extensions.jda.fullName
import me.aberrantfox.kjdautils.extensions.jda.getRoleByName
import me.aberrantfox.kjdautils.extensions.jda.sendPrivateMessage
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.aberrantfox.kjdautils.internal.arguments.MemberArg
import me.aberrantfox.kjdautils.internal.arguments.SentenceArg
import me.aberrantfox.kjdautils.internal.arguments.UserArg
import me.aberrantfox.kjdautils.internal.command.Fail
import me.aberrantfox.kjdautils.internal.command.Pass
import me.aberrantfox.kjdautils.internal.command.precondition
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import java.awt.Color
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.system.measureTimeMillis

const val modCommandCategoryName = "Moderation"

fun modLog(actor: Member, action: String, target: User, reason: String, embedColor: Color = Color.RED) {
    val modLogEmbed = embed {
        title = "User $action!"
        color = embedColor

        field {
            name = "$action user"
            value = "${target.asMention} (${target.asTag} | ${target.id})"
        }

        field {
            name = "Requested by"
            value = "${actor.asMention} (${actor.user.asTag} | ${actor.id})"
        }

        field {
            name = "Reason"
            value = reason
        }

        field {
            name = "Timestamp"
            value = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now())
        }
    }

    target.sendPrivateMessage(modLogEmbed)

    actor.guild.getTextChannelById(modLogChannelID)?.sendMessage(modLogEmbed)?.complete()
}

@Precondition
fun isMod() = precondition {
    if (it.container[it.commandStruct.commandName]?.category != modCommandCategoryName) {
        return@precondition Pass
    }

    if (it.message.member?.hasPermission(Permission.BAN_MEMBERS) == true) {
        return@precondition Pass
    } else {
        return@precondition Fail("You must be a mod (or above) to run this command!")
    }
}

val reasonArg = SentenceArg.makeOptional("No reason provided.")

//TODO: fix delDays; maybe make it an arg or something?
@CommandSet(modCommandCategoryName)
fun modCommands() = commands {
    command("Ban") {
        description = "Bans someone in the guild for a given reason"

        execute(MemberArg, reasonArg) {
            modLog(it.author.toMember(it.guild!!)!!, "Banned", it.args.first.user, it.args.second)
            it.guild?.ban(it.args.first, 0)?.complete()
        }
    }

    command("Unban") {
        description = "Unbans someone in the guild"

        execute(UserArg, reasonArg) {
            modLog(it.author.toMember(it.guild!!)!!, "Unbanned", it.args.first, it.args.second)
            it.guild?.unban(it.args.first)?.complete()
        }
    }

    command("Kick") {
        description = "Kick someone in the guild for a given reason"

        execute(MemberArg, reasonArg) {
            modLog(it.author.toMember(it.guild!!)!!, "Kicked", it.args.first.user, it.args.second)
            it.guild?.kick(it.args.first, it.args.second)?.complete()
        }
    }

    command("Warn") {
        description = "Warn an user"

        execute(UserArg, SentenceArg) {
            modLog(it.author.toMember(it.guild!!)!!, "Warned", it.args.first, it.args.second)
        }
    }

    command("DeleteRoles") {
        description = "Delete's the time-based roles"
        requiresGuild = true

        execute {
            for (roleConfig in memberShipRoles) {
                it.guild?.getRoleByName(roleConfig.name)?.delete()
                            ?.reason("Requested by: ${it.author.fullName()}")
                            ?.complete()
            }

            it.respond("Done")
        }
    }

    command("RefreshRoles") {
        description = "Refreshes the time-based roles"
        requiresGuild = true

        execute {
            val roles = hashMapOf<MembershipTimeRole, Role>()

            for (roleConfig in memberShipRoles) {
                val roleByName = it.guild?.getRoleByName(roleConfig.name)

                if (roleByName == null) {
                    val newRole = it.guild!!.createRole().complete()
                    newRole.manager.setName(roleConfig.name)
                            .setColor(roleConfig.color)
                            .setHoisted(false)
                            .setPermissions(it.guild!!.publicRole.permissions)
                            .revokePermissions(Permission.MESSAGE_MENTION_EVERYONE)
                            .complete()

                    roles[roleConfig] = newRole
                } else {
                    roles[roleConfig] = roleByName
                }
            }

            val sortedRoles = roles.toSortedMap(
                    compareBy<MembershipTimeRole> { it.requiredTimeInDays })

            val timesPerMember = mutableListOf<Long>()

            runBlocking {
                for (member in it.guild!!.members) {
                    launch {
                        timesPerMember.add(measureTimeMillis {
                            val daysOnGuild = ChronoUnit.DAYS.between(member.timeJoined.toLocalDate(), LocalDate.now())

                            val correctRole = sortedRoles
                                    .toList()
                                    .findLast { daysOnGuild >= it.first.requiredTimeInDays }

                            if (correctRole?.second !in member.roles) {
                                println("Modifying role for: ${member.effectiveName}")
                                for (role in sortedRoles) {
                                    if (role.value in member.roles) {
                                        try {
                                            it.guild!!.removeRoleFromMember(member, role.value).complete()
                                        } catch (e: Exception) {
                                            // ignore this, this means the role has already been removed
                                        }
                                    }
                                }

                                if (correctRole != null) {
                                    it.guild!!.addRoleToMember(member, correctRole.second).complete()
                                }
                            }
                        })
                    }
                }
            }

            it.respond("Done")
            println("Took ${timesPerMember.max()} ms per user max, ${timesPerMember.min()} minimum, and ${timesPerMember.average()} average")
        }
    }
}

class EditDeleteManager {
    @Subscribe
    fun onMessageDelete(event: MessageDeleteEvent) {
        /*val message = event.channel.history.getMessageById(event.messageIdLong)

        println(message!!.contentRaw)

        event.jda.getTextChannelById(editDeleteChannelID)?.sendMessage(
                embed {
                    title = "Message Deletion"
                    color = Color(0xff0000)
                    timeStamp = LocalDateTime.now()


                    field {
                        name = "Author"
                        value = message.author.fullName()
                    }
                }
        )?.complete()*/
    }

    @Subscribe
    fun onMessageUpdate(event: MessageUpdateEvent) {

    }
}