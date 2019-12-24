package com.github.dansman805.discordbot.commands

import me.aberrantfox.kjdautils.api.dsl.command.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.discord.User
import java.awt.Color


@CommandSet("Moderation Tools")
fun moderationCommands() = commands {
    command() {
        execute {
            it.author
        }
    }
}

fun modLog(actor: User, action: String, target: User, reason: String, embedColor: Color = Color.RED,
           globalModLog: Boolean = true) {
    val modLogEmbed = embed {
        title = "User $action!"
        color = embedColor

        field {
            name = "${action.capitalize()} user"
            value = "${target}"
        }
    }
}