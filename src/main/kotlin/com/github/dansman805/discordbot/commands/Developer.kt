package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.botConfig
import com.github.dansman805.discordbot.db
import com.github.dansman805.discordbot.entities.MessageDatabaseEntry
import com.github.dansman805.discordbot.entities.Messages
import com.github.dansman805.discordbot.services.CodeEvalService
import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.annotation.Precondition
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.internal.arguments.MessageArg
import me.aberrantfox.kjdautils.internal.arguments.SentenceArg
import me.aberrantfox.kjdautils.internal.command.Fail
import me.aberrantfox.kjdautils.internal.command.Pass
import me.aberrantfox.kjdautils.internal.command.precondition
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.forEach
import me.liuwj.ktorm.entity.sequenceOf

const val developerCategoryName = "Developer"

@Precondition
fun isDeveloper() = precondition {
    if (it.container[it.commandStruct.commandName]?.category != developerCategoryName) {
        return@precondition Pass
    }

    if (botConfig.developerIDs.contains(it.author.idLong)) {
        return@precondition Pass
    } else {
        return@precondition Fail("You must be a developer to run this command!")
    }
}

@CommandSet(developerCategoryName)
fun developerCommands(evalService: CodeEvalService) = commands {

    command("Eval", "Evaluate") {
        execute(SentenceArg("Code")) {
            val rawCode = it.args.first
            it.message.contentRaw
            val code = rawCode.replace("```.*", "").replace("`", "")

            val result = evalService.runCode(code, it.author.jda)

            it.respond(when (result) {
                null -> "Done."
                else -> "```$result```"
            })
        }
    }

    command("AddMessage") {
        execute(MessageArg) {
            val m = it.args.first

            MessageDatabaseEntry {
                messageId = m.idLong
                authorId = m.author.idLong
                channelId = m.textChannel.idLong
                guildId = m.textChannel.guild.idLong

                contentRaw = m.contentRaw
            }
        }
    }

    command("ReadDB") {
        execute {
            val sb = StringBuilder()

            db.sequenceOf(Messages).forEach {
                sb.append(it.contentRaw)
            }
        }
    }
}