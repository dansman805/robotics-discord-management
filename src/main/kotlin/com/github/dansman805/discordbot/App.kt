package com.github.dansman805.discordbot

import me.jakejmattson.kutils.api.dsl.configuration.startBot

fun main() {
    initDb()

    startBot(botConfig.discordToken, true) {
        configure {
            prefix {
                "+"
            }
        }
    }
}

