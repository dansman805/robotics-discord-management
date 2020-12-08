package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.botConfig
import com.github.dansman805.discordbot.dataclasses.MembershipTimeRole
import com.github.dansman805.discordbot.extensions.*
import me.jakejmattson.kutils.api.annotations.CommandSet
import me.jakejmattson.kutils.api.annotations.Precondition
import me.jakejmattson.kutils.api.arguments.EveryArg
import me.jakejmattson.kutils.api.arguments.MemberArg
import me.jakejmattson.kutils.api.arguments.RoleArg
import me.jakejmattson.kutils.api.arguments.UserArg
import me.jakejmattson.kutils.api.dsl.command.commands
import me.jakejmattson.kutils.api.dsl.embed.embed
import me.jakejmattson.kutils.api.dsl.preconditions.Fail
import me.jakejmattson.kutils.api.dsl.preconditions.Pass
import me.jakejmattson.kutils.api.dsl.preconditions.precondition
import me.jakejmattson.kutils.api.extensions.jda.fullName
import me.jakejmattson.kutils.api.extensions.jda.getRoleByName
import me.jakejmattson.kutils.api.extensions.jda.sendPrivateMessage
import me.jakejmattson.kutils.api.extensions.jda.toMember
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.Role
import java.awt.Color
import java.time.Clock
import java.time.LocalDate
import java.time.OffsetDateTime
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
            name = "Timestamp (UTC)"
            value = botConfig.dateTimeFormatter.format(OffsetDateTime.now(Clock.systemUTC()))
        }
    }

    if (actor.guild.isMember(target)) {
        target.sendPrivateMessage(modLogEmbed)
    }

    actor.guild.getTextChannelById(botConfig.modLogChannelId)?.sendMessage(modLogEmbed)?.complete()
}

@Precondition
fun isMod() = precondition {
    if (it.container[it.command!!.names.first()]?.category != modCommandCategoryName) {
        return@precondition Pass
    }

    if (it.message.member?.hasPermission(Permission.BAN_MEMBERS) == true) {
        return@precondition Pass
    } else {
        return@precondition Fail("You must be a mod (or above) to run this command!")
    }
}

val reasonArg = EveryArg.makeOptional("No reason provided.")

//TODO: fix delDays; maybe make it an arg or something?
@CommandSet(modCommandCategoryName)
fun modCommands() = commands {
    command("Ban") {
        description = "Bans someone in the guild for a given reason"

        requiresGuild = true

        execute(MemberArg, reasonArg) {
            it.safe {
                it.author.toMember(it.guild!!)!!.ifHasPermission(it.channel, Permission.BAN_MEMBERS) {
                    it.guild?.ban(it.args.first, 0)?.complete()
                    modLog(it.author.toMember(it.guild!!)!!, "Banned", it.args.first.user, it.args.second)
                }
            }
        }
    }

    command("Unban") {
        description = "Unbans someone in the guild"

        execute(UserArg, reasonArg) {
            it.safe {
                modLog(it.author.toMember(it.guild!!)!!, "Unbanned", it.args.first, it.args.second)
                it.guild?.unban(it.args.first)?.complete()
            }
        }
    }

    command("Kick") {
        description = "Kick someone in the guild for a given reason"

        execute(MemberArg, reasonArg) {
            it.safe {
                modLog(it.author.toMember(it.guild!!)!!, "Kicked", it.args.first.user, it.args.second)
                it.guild?.kick(it.args.first, it.args.second)?.complete()
            }
        }
    }

    command("Warn") {
        description = "Warn a member"

        execute(MemberArg, reasonArg) {
            it.safe {
                modLog(it.author.toMember(it.guild!!)!!, "Warned", it.args.first.getUser(), it.args.second)
            }
        }
    }

    command("DeleteRoles") {
        description = "Delete's the time-based roles"
        requiresGuild = true

        execute {
            it.safe {
                for (roleConfig in botConfig.membershipRoles) {

                    it.guild?.getRoleByName(roleConfig.name)
                            ?.delete()
                            ?.reason("Requested by: ${it.author.fullName()}")
                            ?.complete()
                }

                it.respond("Done")
            }
        }
    }

    command("RefreshRoles") {
        description = "Refreshes the time-based roles"
        requiresGuild = true

        execute {
            it.safe { event ->
                val roles = hashMapOf<MembershipTimeRole, Role>()

                for (roleConfig in botConfig.membershipRoles) {
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
                        compareBy { it.requiredTimeInDays })

                val joinedLogs = it.author.jda.getTextChannelById(botConfig.joinedLogId)!!.allMessages()

                it.guild!!.retrieveMembers().complete(null)

                event.guild!!.retrieveMembers()
                        .thenApply { v -> event.guild!!.getMemberCache() }
                        .thenAccept {
                            it.forEach { member ->
                                val daysOnGuild = ChronoUnit.DAYS.between(
                                        member.firstJoin(joinedLogs).toLocalDate(), LocalDate.now())

                                val correctRole = sortedRoles
                                        .toList()
                                        .findLast { daysOnGuild >= it.first.requiredTimeInDays }

                                if (correctRole?.second !in member.roles) {
                                    println("Assigning ${member.effectiveName}: ${correctRole?.second?.name}, days on guild: $daysOnGuild")

                                    for (role in sortedRoles) {
                                        if (role.value in member.roles) {
                                            try {
                                                event.guild!!.removeRoleFromMember(member, role.value).submit()
                                            } catch (e: Exception) {
                                                // ignore this, this means the role has already been removed
                                            }
                                        }
                                    }

                                    if (correctRole != null) {
                                        event.guild!!.addRoleToMember(member, correctRole.second).submit()
                                    }
                                }
                            }
                        }
                        .thenRun {
                            event.guild!!.pruneMemberCache()
                            it.respond("Done")
                        }
            }
        }

        command("RoleStatistics", "RoleStat", "RoleStats", "RoleInformation", "RoleInfo") {
            description = "Shows the number of users with a given role or all the roles"

            execute(RoleArg.makeNullableOptional()) {
                it.safe {
                    if (it.args.first != null) {
                        val role = it.args.first!!

                        it.respond(embed {
                            title {
                                text = "${role.name} Information"
                            }
                            color = role.color

                            field {
                                name = "Members with the Role"
                                value = role.memberCount().toString()
                                inline = true
                            }

                            field {
                                name = "Color"
                                value = role.color?.toHexString() ?: "N/A"
                                inline = true
                            }
                        })
                    } else {
                        val roleList = it.guild?.roles?.sortedByDescending { it.memberCount() } ?: emptyList()

                        val embeds = MutableList(roleList.size) { EmbedBuilder() }

                        for (i in embeds.indices) {
                            val embed = embeds[i / 25]
                            val role = roleList[i]

                            embed.addField(role.name, role.memberCount().toString(), true)
                        }

                        for (embed in embeds) {
                            it.respond(embed.build())
                        }
                    }
                }
            }
        }
    }

    command("MemberFirstJoin") {
        requiresGuild = true
        execute(MemberArg) {
            it.safe {
                it.respond(botConfig.dateTimeFormatter.format(
                        it.args.first.firstJoin(
                                it.author.jda.getTextChannelById(botConfig.joinedLogId)!!.allMessages())
                )
                )
            }
        }
    }
}
