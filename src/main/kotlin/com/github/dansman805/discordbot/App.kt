package com.github.dansman805.discordbot

import me.aberrantfox.kjdautils.api.dsl.PrefixDeleteMode
import me.aberrantfox.kjdautils.api.startBot

fun main() {
    initDb()

    startBot(botConfig.discordToken) {
        configure {
            deleteMode = PrefixDeleteMode.None
            prefix = "+"
        }
    }
}

