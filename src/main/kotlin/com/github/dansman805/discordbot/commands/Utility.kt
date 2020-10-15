package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.extensions.safe
import com.github.dansman805.discordbot.extensions.thumbUpDown
import com.github.dansman805.discordbot.services.TeamService
import com.github.dansman805.discordbot.services.WikipediaSummaryService
import kotlinx.serialization.ImplicitReflectionSerializer
import me.jakejmattson.kutils.api.annotations.CommandSet
import me.jakejmattson.kutils.api.annotations.Precondition
import me.jakejmattson.kutils.api.arguments.EveryArg
import me.jakejmattson.kutils.api.dsl.arguments.ArgumentResult
import me.jakejmattson.kutils.api.dsl.arguments.ArgumentType
import me.jakejmattson.kutils.api.dsl.command.CommandEvent
import me.jakejmattson.kutils.api.dsl.command.commands
import me.jakejmattson.kutils.api.dsl.preconditions.Fail
import me.jakejmattson.kutils.api.dsl.preconditions.Pass
import me.jakejmattson.kutils.api.dsl.preconditions.precondition
import me.jakejmattson.kutils.api.extensions.jda.toMember
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.exceptions.HierarchyException

open class TeamNumberArg : ArgumentType<Int>() {
    companion object : TeamNumberArg()

    override val name = "Team Number"
    override fun generateExamples(event: CommandEvent<*>) = arrayListOf("11115")

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Int> {
        val int = arg.toIntOrNull() ?: return ArgumentResult.Error("Expected a team number, got $arg")
        return ArgumentResult.Success(int)
    }
}

@Precondition
fun hasNickPerms() = precondition {
    if (it.command!!.names.first() != "SetNickname") {
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
        requiresGuild = false
        description = "Responds with Pong! (As well as the server name, and the time it takes the bot to respond)"
        execute {
            it.safe {
                it.respond("""Pong! We're in the **${it.guild?.name}** server.
                |Took ${it.channel.jda.restPing.complete()} ms to respond.""".trimMargin())
            }
        }
    }

    command("SetNickname", "SetNick", "Nickname", "Nick") {
        description = "Allows a member to change their nickname."
        execute(EveryArg) {
            it.safe {
                val nickToChangeTo = it.args.first
                val truncatedNick =
                        if (nickToChangeTo.length > 32) nickToChangeTo.substring(0..31)
                        else nickToChangeTo

                try {
                    it.message.member?.modifyNickname(truncatedNick)?.complete()
                    it.respond("Nick successfully changed to $truncatedNick")

                    if (nickToChangeTo.length > 32) {
                        it.respond("Warning: truncated nickname to 32 characters.")
                    }
                }
                catch (e: HierarchyException) {
                    it.respond("Your top role is the same as or higher than mine!")
                }
            }
        }
    }

    command("Vote") {
        requiresGuild = true

        execute(EveryArg) {
            it.message.thumbUpDown()
        }
    }

    command("TheOrangeAlliance", "TOA") {
        requiresGuild = false

        description = "Provides data on a given FTC team from The Orange Alliance"

        execute(TeamNumberArg) {
            it.safe {
                try {
                    it.respond(
                            teamService.getFTCTeam(it.args.component1()).genEmbed()
                    )
                } catch (e: Exception) {
                    it.respond("This team does not have any data on it yet, or it does not exist!")
                }
            }
        }

    }

    command("TheBlueAlliance", "TBA") {
        requiresGuild = false

        description = "Provides data on a given FRC team from The Blue Alliance"

        execute(TeamNumberArg) {
            it.safe {
                try {
                    it.respond(
                            teamService.getFRCTeam(it.args.component1()).genEmbed()
                    )
                } catch (e: Exception) {
                    it.respond("This team does not have any data on it yet, or it does not exist!")
                }
            }
        }
    }

    command("Wikipedia", "Wiki", "W") {
        requiresGuild = false

        description = "Provides the Wikipedia summary on a given topic"

        execute(EveryArg) {
            it.safe {
                val summary = wikipediaSummaryService.getSummary(it.args.first)

                if (summary?.toEmbed() != null) {
                    it.respond(summary.toEmbed())
                } else {
                    it.respond("No Wikipedia article found!")
                }
            }
        }
    }
}
