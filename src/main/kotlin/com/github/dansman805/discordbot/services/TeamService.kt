package com.github.dansman805.discordbot.services

import com.github.dansman805.discordbot.botConfig
import com.github.dansman805.discordbot.dataclasses.FRCTeam
import com.github.dansman805.discordbot.dataclasses.FTCTeam
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.serialization.responseObject
import kotlinx.serialization.json.Json
import me.jakejmattson.discordkt.api.annotations.Service

@kotlinx.serialization.ImplicitReflectionSerializer
@kotlinx.serialization.UnstableDefault
@Service
class TeamService {
    fun getFTCTeam(number: Int): FTCTeam {
        val result = "https://theorangealliance.org/api/team/$number"
                .httpGet()
                .header("Content-Type", "application/json")
                .header("X-TOA-Key", botConfig.orangeAllianceKey)
                .header("X-Application-Origin", botConfig.appName)
                .responseString().third

        val json = Json { ignoreUnknownKeys = false }

        return json.parse(
                FTCTeam.serializer(),
                result.get().substring(1..result.get().length - 2)
        )
    }

    fun getFRCTeam(number: Int): FRCTeam {
        val result = Fuel.get("https://www.thebluealliance.com/api/v3/team/frc$number")
                .header("Content-Type", "application/json")
                .header("X-TBA-Auth-Key", botConfig.blueAllianceKey)
                .header("User-Agent", botConfig.appName)
                .responseObject<FRCTeam>(json = Json { ignoreUnknownKeys = false }).third

        return result.get()
    }
}