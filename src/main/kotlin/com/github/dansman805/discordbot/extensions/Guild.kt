package com.github.dansman805.discordbot.extensions

import com.github.dansman805.discordbot.botConfig
import com.gitlab.kordlib.core.behavior.channel.createEmbed
import com.gitlab.kordlib.core.entity.Guild
import com.gitlab.kordlib.core.entity.channel.TextChannel
import com.gitlab.kordlib.rest.Image
import kotlinx.coroutines.flow.count
import java.awt.Color
import java.time.ZoneOffset

suspend fun Guild.embedOfGuild(textChannel: TextChannel)  {
    textChannel.createEmbed {
        val g = this@embedOfGuild

        title = g.name
        color = Color(64, 0, 255)

        val iconUrl = g.getIconUrl(Image.Format.PNG)

        if (iconUrl != null) {
            thumbnail {
                url = iconUrl
            }
        }

        field {
            name = "Name"
            value = g.name
        }

        field {
            name = "ID"
            value = g.id.value
        }

        field {
            name = "Created At (UTC)"
            value = g.id.formattedTimeStamp
        }

        field {
            name = "Owner"
            value = g.owner.nicknameMention
        }

        field {
            name = "Member Count"
            value = g.members.count().toString()
        }

        field {
            name = "Channel Count"
            value = g.channels.count().toString()
        }

        field {
            name = "Role Count"
            value = (g.channels.count() - 1).toString()
        }

        field {
            value = g.emojis.count().toString()
            name = "Emoji Count"
        }

        field {
            name = "Region"
            value = g.getRegion().name
        }

        field {
            name = "Icon URL"
            value = iconUrl ?: "n/a"
        }
    }
}