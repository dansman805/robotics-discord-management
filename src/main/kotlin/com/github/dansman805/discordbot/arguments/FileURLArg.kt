package com.github.dansman805.discordbot.arguments

import me.jakejmattson.discordkt.api.arguments.ArgumentResult
import me.jakejmattson.discordkt.api.arguments.ArgumentType
import me.jakejmattson.discordkt.api.arguments.Error
import me.jakejmattson.discordkt.api.arguments.Success
import me.jakejmattson.discordkt.api.dsl.CommandEvent

open class FileURLArg(override val name: String = "File") : ArgumentType<String>() {
    /**
     * Accepts a file as a message attachment.
     */
    companion object : FileURLArg()

    override suspend fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<String> {
        val attachments = event.message.attachments

        if (attachments.isEmpty())
            return Error("No attachments")

        val file = attachments.first().url

        return Success(file, 0)
    }

    override fun generateExamples(event: CommandEvent<*>) = listOf("File")
    override fun formatData(data: String) = data
}