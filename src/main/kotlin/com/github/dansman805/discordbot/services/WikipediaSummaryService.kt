package com.github.dansman805.discordbot.services

import com.github.dansman805.discordbot.dataclasses.WikipediaSummary
import com.github.kittinunf.fuel.Fuel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import me.jakejmattson.kutils.api.annotations.Service

@kotlinx.serialization.ImplicitReflectionSerializer
@kotlinx.serialization.UnstableDefault
@Service
class WikipediaSummaryService {
    fun getSummary(topic: String): WikipediaSummary? {

        val result = Fuel.get("https://en.wikipedia.org/api/rest_v1/page/summary/$topic").responseString()

        if (result.third.component2() != null) {
            return null
        }

        val json = Json(JsonConfiguration(ignoreUnknownKeys = true))

        return json.parse(WikipediaSummary.serializer(), result.third.get())
    }
}