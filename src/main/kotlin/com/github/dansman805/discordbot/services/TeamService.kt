package com.github.dansman805.discordbot.services

import com.github.dansman805.discordbot.appName
import com.github.dansman805.discordbot.blueAllianceKey
import com.github.dansman805.discordbot.orangeAllianceKey
import com.github.dansman805.discordbot.schemas.FRCTeam
import com.github.dansman805.discordbot.schemas.FTCTeam
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.serialization.responseObject
import kotlinx.serialization.json.Json
import me.aberrantfox.kjdautils.api.annotation.Service

@kotlinx.serialization.ImplicitReflectionSerializer
@kotlinx.serialization.UnstableDefault
@Service
class TeamService {
    fun getFTCTeam(number: Int): FTCTeam {
        val result = "https://theorangealliance.org/api/team/${number}"
                .httpGet()
                .header("Content-Type", "application/json")
                .header("X-TOA-Key", orangeAllianceKey)
                .header("X-Application-Origin", appName)
                .responseString().third

        val json = Json.nonstrict

        return json.parse(
                FTCTeam.serializer(),
                result.get().substring(1..result.get().length - 2)
        )
    }

    fun getFRCTeam(number: Int): FRCTeam {
        val result = Fuel.get("https://www.thebluealliance.com/api/v3/team/frc${number}")
                .header("Content-Type", "application/json")
                .header("X-TBA-Auth-Key", blueAllianceKey)
                .header("User-Agent", appName)
                .responseObject<FRCTeam>(json = Json.nonstrict).third

        return  result.get()
    }
}