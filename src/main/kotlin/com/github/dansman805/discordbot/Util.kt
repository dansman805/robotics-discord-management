package com.github.dansman805.discordbot

import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.extensions.jda.fullName
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageEmbed
import java.awt.Color

fun Color.toRGB() = "R: ${this.red}, G: ${this.green}, B: ${this.blue}"

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
        value = m.timeCreated.format(dateTimeFormatter)
    }

    field {
        name = "Joined Guild"
        value = m.timeJoined.format(dateTimeFormatter)
    }

    field {
        name = "Color"
        value = if (m.color == null) "n/a" else m.color?.toRGB()
    }

    var roleNames = ""

    m.roles.forEach {
        roleNames += "${it.name}, "
    }

    field {
        name = "Roles"
        value = if (roleNames.length >= 2) roleNames.substring(0, roleNames.length-2) else "None"
    }

    field {
        name = "Icon URL"
        value = iconUrl
    }
}

fun Guild.toEmbed() =  embed {
    val g = this@toEmbed

    color = Color(64, 0, 255)

    if (g.iconUrl != null) {
        thumbnail = g.iconUrl
    }

    field {
        name = "Name"
        value = g.name
    }

    field {
        name = "ID"
        value = g.id
    }

    field {
        name = "Created At"
        value = g.timeCreated.format(dateTimeFormatter)
    }

    field {
        name = "Owner"
        value = g.owner?.fullName()
    }

    field {
        name = "Member Count"
        value = g.members.size.toString()
    }

    field {
        name = "Channel Count"
        value = g.channels.size.toString()
    }

    field {
        name = "Role Count"
        value = (g.channels.size - 1).toString()
    }

    field {
        value = g.emotes.size.toString()
        name = "Emoji Count"
    }

    field {
        name = "Region"
        value = g.region.name
    }

    field {
        name = "Icon URL"
        value = g.iconUrl ?: "n/a"
    }
}