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
        val allowableCharactersInMessage = DISCORD_MESSAGE_CHARACTER_LIMIT - CODE_BLOCK_DELIMITER.length * 2

        val sw = StringWriter()
        val pw = PrintWriter(sw)

        e.printStackTrace(pw)

        val errorMessage = sw.toString()
        val sections = mutableListOf<String>()

        for (i in errorMessage.indices step allowableCharactersInMessage) {
            sections.add(
                    errorMessage.substring(
                            i until
                                    minOf(i+allowableCharactersInMessage,
                                            errorMessage.length-1)
                    )
            )
        }

        sections.forEach {
            this.channel.sendMessage(CODE_BLOCK_DELIMITER + it + CODE_BLOCK_DELIMITER).submit()
        }

        botConfig.developerIds.forEach {
            this.channel.sendMessage("<@$it>").submit()
        }
    }
}
