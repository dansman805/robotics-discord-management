package com.github.dansman805.discordbot.extensions

import com.github.dansman805.discordbot.botConfig
import me.aberrantfox.kjdautils.api.dsl.embed
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import java.time.OffsetDateTime
import kotlin.math.min

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

    //field {
    //    name = "Days Since First Message on Guild"
    //    value = daysSinceFirstMessage().toString()
    //}
}

fun Member.firstJoin(joinLogs: List<Message>): OffsetDateTime =
        minOf(
            this.timeJoined,
            joinLogs
                .sortedBy { it.timeCreated }
                .firstOrNull { it.isMentioned(this, Message.MentionType.USER) }
                ?.timeCreated ?: this.timeJoined
        )