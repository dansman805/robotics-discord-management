package com.github.dansman805.discordbot

import com.github.dansman805.discordbot.commands.EditDeleteReciever
import me.aberrantfox.kjdautils.api.dsl.PrefixDeleteMode
import me.aberrantfox.kjdautils.api.startBot

fun main() {
    startBot(discordToken) {
        configure {
            deleteMode = PrefixDeleteMode.None
            prefix = "+"
        }

        registerListeners(EditDeleteReciever())
    }
}

