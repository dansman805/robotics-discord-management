package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.appName
import com.github.dansman805.discordbot.blueAllianceKey
import com.github.dansman805.discordbot.orangeAllianceKey
import com.github.dansman805.discordbot.schemas.FRCTeam
import com.github.dansman805.discordbot.schemas.FTCTeam
import com.github.dansman805.discordbot.services.TeamService
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.serialization.responseObject
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import me.aberrantfox.kjdautils.api.dsl.command.CommandEvent
import me.aberrantfox.kjdautils.api.dsl.command.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.internal.command.ArgumentResult
import me.aberrantfox.kjdautils.internal.command.ArgumentType
import me.aberrantfox.kjdautils.internal.command.ConsumptionType
import java.lang.Exception



open class TeamNumberArg : ArgumentType<Int>() {
    companion object : TeamNumberArg()

    override val name = "Team Number"
    override val examples = arrayListOf("11115")
    override val consumptionType = ConsumptionType.Single

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Int> {
        val int = arg.toIntOrNull() ?: return ArgumentResult.Error("Expected a team number, got $arg")
        return ArgumentResult.Success(int)
    }
}

@ImplicitReflectionSerializer
@CommandSet("Utility")
fun utilityCommands(teamService: TeamService) = commands {
    command("Ping") {
        description = "Responds with Pong! (As well as the server name, and the time it takes the bot to respond)"
        execute {
            it.respond("""Pong! We're in the **${it.guild?.name}** server.
                |Took ${it.channel.jda.restPing.complete()} ms to respond.""".trimMargin())
        }
    }

    command("TheOrangeAlliance", "TOA") {
        description = "Provides data on a given FTC team from The Orange Alliance"

        execute (TeamNumberArg) { event ->


            try {
                event.respond(
                        teamService.getFTCTeam(event.args.component1()).genEmbed()
                )
            }
            catch (e: Exception) {
                event.respond("This team does not have any data on it yet, or it does not exist!")
            }
        }

    }

    command("TheBlueAlliance", "TBA") {
        description = "Provides data on a given FRC team from The Blue Alliance"

        execute (TeamNumberArg) { event ->
            try {
                event.respond(
                        teamService.getFRCTeam(event.args.component1()).genEmbed()
                )
            }
            catch (e: Exception) {
                event.respond("This team does not have any data on it yet, or it does not exist!")
            }
        }
    }
}
