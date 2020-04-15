package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.botConfig
import com.github.dansman805.discordbot.services.CodeEvalService
import com.github.dansman805.discordbot.services.jda
import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.annotation.Precondition
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.internal.arguments.Manual
import me.aberrantfox.kjdautils.internal.arguments.SentenceArg
import me.aberrantfox.kjdautils.internal.command.Fail
import me.aberrantfox.kjdautils.internal.command.Pass
import me.aberrantfox.kjdautils.internal.command.precondition
import javax.script.ScriptEngineManager

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
            val code = rawCode.replace("```.*", "").replace("`", "")

            val result = evalService.runCode(code, it.author.jda)

            if (result != null) {
                it.respond(result)
            }
        }
    }
}