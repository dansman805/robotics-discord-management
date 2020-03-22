package com.github.dansman805.discordbot

import com.github.dansman805.discordbot.commands.EditDeleteManager
import me.aberrantfox.kjdautils.api.dsl.PrefixDeleteMode
import me.aberrantfox.kjdautils.api.startBot

fun main() {
    startBot(discordToken) {
        configure {
            deleteMode = PrefixDeleteMode.None
            prefix = "+"
        }

        registerListeners(EditDeleteManager())
    }
}

