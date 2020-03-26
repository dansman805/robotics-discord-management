package com.github.dansman805.commands

import com.github.dansman805.discordbot.services.TeamService
import com.github.dansman805.discordbot.services.WikipediaSummaryService
import kotlinx.serialization.ImplicitReflectionSerializer
import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.annotation.Precondition
import me.aberrantfox.kjdautils.api.dsl.command.CommandEvent
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.aberrantfox.kjdautils.internal.arguments.SentenceArg
import me.aberrantfox.kjdautils.internal.command.*
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.exceptions.HierarchyException


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

@Precondition
fun hasNickPerms() = precondition {
    if (it.commandStruct.commandName != "SetNickname") {
        return@precondition Pass
    }

    if (it.author.toMember(it.guild!!)?.hasPermission(Permission.NICKNAME_CHANGE) == true) {
        return@precondition Pass
    } else {
        return@precondition Fail("You do not have permissions to set your own nickname!")
    }
}

@CommandSet("Utility")
@ImplicitReflectionSerializer
@kotlinx.serialization.UnstableDefault
fun utilityCommands(teamService: TeamService, wikipediaSummaryService: WikipediaSummaryService) = commands {
    command("Ping") {
        description = "Responds with Pong! (As well as the server name, and the time it takes the bot to respond)"
        execute {
            it.respond("""Pong! We're in the **${it.guild?.name}** server.
                |Took ${it.channel.jda.restPing.complete()} ms to respond.""".trimMargin())
        }
    }

    command("SetNickname", "SetNick", "Nickname", "Nick") {
        description = "Allows a member to change their nickname."
        execute(SentenceArg) {
            val nickToChangeTo = it.args.first
            val truncatedNick =
                    if (nickToChangeTo.length > 32) nickToChangeTo.substring(0..31)
                    else nickToChangeTo

            try {
                it.message.member?.modifyNickname(truncatedNick)?.complete()
            } catch (e: HierarchyException) {
                it.respond("Your top role is the same as or higher than mine!")
                return@execute
            }

            it.respond("Nick succesfully changed to $truncatedNick")

            if (nickToChangeTo.length > 32) {
                it.respond("Warning: truncated nickname to 32 characters.")
            }


        }
    }

    command("TheOrangeAlliance", "TOA") {
        description = "Provides data on a given FTC team from The Orange Alliance"

        execute(TeamNumberArg) { event ->
            try {
                event.respond(
                        teamService.getFTCTeam(event.args.component1()).genEmbed()
                )
            } catch (e: Exception) {
                event.respond("This team does not have any data on it yet, or it does not exist!")
            }
        }

    }

    command("TheBlueAlliance", "TBA") {
        description = "Provides data on a given FRC team from The Blue Alliance"

        execute(TeamNumberArg) { event ->
            try {
                event.respond(
                        teamService.getFRCTeam(event.args.component1()).genEmbed()
                )
            } catch (e: Exception) {
                event.respond("This team does not have any data on it yet, or it does not exist!")
            }
        }
    }

    command("Wikipedia", "Wiki", "W") {
        description = "Provides the Wikipedia summary on a given topic"

        execute(SentenceArg) {
            val summary = wikipediaSummaryService.getSummary(it.args.first)


            if (summary?.toEmbed() != null) {
                it.respond(summary.toEmbed())
            }
            else {
                it.respond("No Wikipedia article found!")
            }
        }
    }
}
