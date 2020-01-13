package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.appName
import com.github.dansman805.discordbot.blueAllianceKey
import com.github.dansman805.discordbot.orangeAllianceKey
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.serialization.responseObject
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import me.aberrantfox.kjdautils.api.dsl.command.CommandEvent
import me.aberrantfox.kjdautils.api.dsl.command.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.internal.command.ArgumentResult
import me.aberrantfox.kjdautils.internal.command.ArgumentType
import me.aberrantfox.kjdautils.internal.command.ConsumptionType
import java.awt.Color
import java.lang.Exception

@Serializable
data class FTCTeam(val team_key: String?,
                val region_key: String?,
                val league_key: String?,
                val team_number: Int?,
                val team_name_short: String?,
                val team_name_long: String?,
                val robot_name: String?,
                val last_active: Int?,
                val city: String?,
                val state_prov: String?,
                val zip_code: String?,
                val country: String?,
                val rookie_year: Int?,
                val website: String?) {
    fun location() = "$city, $state_prov $zip_code, $country"

    fun genEmbed() = embed {
        title = "FIRST®️ Tech Challenge Team $team_number"
        color = Color(0xf89808)
        thumbnail = "https://raw.githubusercontent.com/orange-alliance/the-orange-alliance/master/src/assets/imgs/icon512.png"

        field {
            name = "Name"
            value = team_name_short
            inline = true
        }

        field {
            name = "Rookie Year"
            value = rookie_year.toString()
            inline = true
        }

        field {
            name = "Location"
            value = location()
            inline = true
        }

        field {
            name = "Website"
            value = website ?: "n/a"
        }

        description = "https://theorangealliance.org/teams/$team_number"
    }
}

@Serializable
data class FRCTeam(
    val key: String?,
    val team_number: Int?,
    val nickname: String?,
    val name: String?,
    val city: String?,
    val state_prov: String?,
    val country: String?,
    val address: String?,
    val postal_code: String?,
    val gmaps_place_id: String?,
    val gmaps_url: String?,
    val lat: Double?,
    val lng: Double?,
    val location_name: String?,
    val website: String?,
    val rookie_year: Int?,
    val motto: String?,
    val home_championship: Map<String?, String?>?) {
    fun location() = "$city, $state_prov $postal_code, $country"

    fun genEmbed() = embed {
        title = "FIRST®️ Robotics Competition Team $team_number"
        color = Color(0x36469C)
        thumbnail = "https://frcavatars.herokuapp.com/get_image?team=$team_number"

        field {
            name = "Name"
            value = nickname
            inline = true
        }

        field {
            name = "Rookie Year"
            value = rookie_year.toString()
            inline = true
        }

        field {
            name = "Location"
            value = location()
            inline = true
        }

        field {
            name = "Website"
            value = website ?: "n/a"
        }

        description = "https://thebluealliance.com/team/$team_number"
    }
}

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
fun utilityCommands() = commands {
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
            val (request, response, result) = "https://theorangealliance.org/api/team/${event.args.component1()}"
                    .httpGet()
                    .header("Content-Type", "application/json")
                    .header("X-TOA-Key", orangeAllianceKey)
                    .header("X-Application-Origin", appName)
                    .responseString()

            try {
                val json = Json(JsonConfiguration.Stable)

                val team = json.parse(
                        FTCTeam.serializer(),
                        result.get().substring(1..result.get().length - 2)
                )

                event.respond(team.genEmbed())
            }
            catch (e: Exception) {
                event.respond("This team does not have any data on it yet, or it does not exist!")
            }
        }

    }

    command("TheBlueAlliance", "TBA") {
        description = "Provides data on a given FRC team from The Blue Alliance"

        execute (TeamNumberArg) { event ->
            val (request, response, result) = "https://www.thebluealliance.com/api/v3/team/frc${event.args.component1()}"
                    .httpGet()
                    .header("Content-Type", "application/json")
                    .header("X-TBA-Auth-Key", blueAllianceKey)
                    .header("User-Agent", appName)
                    .responseObject<FRCTeam>()

            try {
                event.respond(result.get().genEmbed())
            }
            catch (e: Exception) {
                event.respond("This team does not have any data on it yet, or it does not exist!")
            }
        }
    }
}
