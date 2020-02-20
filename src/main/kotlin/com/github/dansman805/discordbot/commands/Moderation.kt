package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.editDeleteChannelID
import com.github.dansman805.discordbot.modLogChannelID
import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.annotation.Precondition
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.extensions.jda.fullName
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
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
}

class EditDeleteReciever : ListenerAdapter() {
    override fun onMessageDelete(event: MessageDeleteEvent) {
        val message = event.channel.retrieveMessageById(event.messageIdLong).complete()

        println(message.contentRaw)

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
        )?.complete()
    }

    override fun onMessageUpdate(event: MessageUpdateEvent) {

    }
}