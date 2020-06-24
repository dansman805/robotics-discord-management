package com.github.dansman805.discordbot.extensions

import com.github.dansman805.discordbot.botConfig
import me.jakejmattson.kutils.api.dsl.command.CommandEvent
import me.jakejmattson.kutils.api.dsl.command.DiscordContext
import me.jakejmattson.kutils.api.dsl.command.GenericContainer
import java.io.StringWriter


const val CODE_BLOCK_DELIMITER = "```"

fun <T : GenericContainer> CommandEvent<T>.safe(pingDevelopers: Boolean = false, thingToRun: (CommandEvent<T>) -> Unit) {
    try {
        thingToRun(this)
    } catch (e: Exception) {
        val sw = StringWriter()

        this.channel.sendMessage(CODE_BLOCK_DELIMITER + e.message + CODE_BLOCK_DELIMITER).submit()

        if (pingDevelopers) {
            botConfig.developerIds.forEach {
                this.channel.sendMessage("<@$it>").submit()
            }
        }

        throw e
    }
}

val <T : GenericContainer> CommandEvent<T>.discordContext: DiscordContext
    get() = DiscordContext(
            this.discord,
            this.message,
            this.author,
            this.guild,
            this.channel
    )