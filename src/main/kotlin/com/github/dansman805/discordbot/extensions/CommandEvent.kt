package com.github.dansman805.discordbot.extensions

import com.github.dansman805.discordbot.botConfig
import me.aberrantfox.kjdautils.api.dsl.command.ArgumentContainer
import me.aberrantfox.kjdautils.api.dsl.command.CommandEvent
import java.io.PrintWriter
import java.io.StringWriter


private const val DISCORD_MESSAGE_CHARACTER_LIMIT = 2000
private const val CODE_BLOCK_DELIMITER = "```"

fun<T: ArgumentContainer> CommandEvent<T>.safe(thingToRun: (CommandEvent<T>) -> Unit) {
    try {
        thingToRun(this)
    }
    catch (e: Exception) {
        val sw = StringWriter()
        val pw = PrintWriter(sw)

        this.channel.sendMessage(CODE_BLOCK_DELIMITER + e.message + CODE_BLOCK_DELIMITER).submit()

        botConfig.developerIds.forEach {
            this.channel.sendMessage("<@$it>").submit()
        }

        throw e
    }
}
