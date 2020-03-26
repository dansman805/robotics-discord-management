package com.github.dansman805.discordbot.extensions

import com.github.dansman805.discordbot.botConfig
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.extensions.jda.fullName
import net.dv8tion.jda.api.entities.Guild
import java.awt.Color

fun Guild.toEmbed() = embed {
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
        value = g.timeCreated.format(botConfig.dateTimeFormatter)
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