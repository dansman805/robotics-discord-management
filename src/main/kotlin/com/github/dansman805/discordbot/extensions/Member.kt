package com.github.dansman805.discordbot.extensions

import com.github.dansman805.discordbot.botConfig
import com.github.dansman805.discordbot.db
import com.github.dansman805.discordbot.entities.Messages
import me.aberrantfox.kjdautils.api.dsl.embed
import me.liuwj.ktorm.dsl.eq
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import java.time.OffsetDateTime
import com.github.dansman805.discordbot.toDateTime
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.liuwj.ktorm.entity.*
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.MessageChannel

fun Member.toEmbed() = embed {
    val m = this@toEmbed
    val iconUrl = m.user.avatarUrl ?: "n/a"

    color = m.color
    if (iconUrl != "n/a") {
        thumbnail = iconUrl
    }

    field {
        name = "Name"
        value = m.effectiveName
    }

    field {
        name = "ID"
        value = m.id
    }

    field {
        name = "Nickname"
        value = m.nickname ?: "n/a"
        inline = m.nickname == null
    }

    field {
        name = if (m.user.isBot) "Bot Created" else "User Joined Discord"
        value = m.timeCreated.format(botConfig.dateTimeFormatter)
    }

    field {
        name = "Joined Guild"
        value = m.timeJoined.format(botConfig.dateTimeFormatter)
    }

    field {
        name = "Color"
        value = if (m.color == null) "n/a" else m.color?.toHexString()
    }

    var roleNames = ""

    m.roles.forEach {
        roleNames += "${it.name}, "
    }

    field {
        name = "Roles"
        value = if (roleNames.length >= 2) roleNames.substring(0, roleNames.length - 2) else "None"
    }

    field {
        name = "Icon URL"
        value = iconUrl
    }
}

var x = 0

private val messageSequence by lazy { db.sequenceOf(Messages) }

fun Member.firstJoin(joinLogs: List<Message>): OffsetDateTime{
    println(x)
    x++

    return minOf(
            minOf(
                    this.timeJoined,
                    joinLogs
                            .sortedBy { it.timeCreated }
                            .firstOrNull { it.isMentioned(this, Message.MentionType.USER) }
                            ?.timeCreated ?: this.timeJoined
            ),
            messageSequence
                    .filter { it.guildId eq this.guild.idLong }
                    .filter { it.authorId eq this.idLong }
                    .minBy { it.epochSecond }
                    ?.toDateTime() ?: this.timeJoined
    )
}

fun Member.ifHasPermission(messageChannel: MessageChannel,
                           vararg permissions: Permission,
                           thingToRunIfPermissionPresent: () -> Unit) {
    var shouldRun = true

    if (!this.hasPermission(permissions.toList())) {
        val permissionsLacked = permissions.filter { !this.hasPermission(it) }

        messageChannel.sendMessage("${this.effectiveName}, " +
                "you do not have ${permissionsLacked.joinToString(separator = " or ") { it.toString() }} permissions.")
                .complete()

        shouldRun = false
    }

    val selfMember = this.jda.selfUser.toMember(this.guild)

    if (selfMember?.hasPermission(permissions.toList()) == false) {
        val permissionsLacked = permissions.filter { !selfMember.hasPermission(it) }

        messageChannel.sendMessage(selfMember.effectiveName +
                " does not have ${permissionsLacked.joinToString(separator = " or ") { it.toString() }} permissions.")
                .complete()

        shouldRun = false
    }
    if (shouldRun) {
        thingToRunIfPermissionPresent()
    }
}