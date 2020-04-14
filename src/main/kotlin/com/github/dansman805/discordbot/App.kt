package com.github.dansman805.discordbot

import com.github.dansman805.discordbot.commands.EditDeleteManager
import com.github.dansman805.discordbot.commands.Infection
import me.aberrantfox.kjdautils.api.dsl.PrefixDeleteMode
import me.aberrantfox.kjdautils.api.startBot

fun main() {
    initDb()

    startBot(botConfig.discordToken) {
        configure {
            deleteMode = PrefixDeleteMode.None
            prefix = "+"
        }

        registerListeners(EditDeleteManager(), Infection())
    }
}

