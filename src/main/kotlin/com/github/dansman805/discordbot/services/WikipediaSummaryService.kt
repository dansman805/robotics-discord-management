package com.github.dansman805.discordbot.services

import com.github.dansman805.discordbot.dataclasses.WikipediaSummary
import com.github.kittinunf.fuel.Fuel
import kotlinx.serialization.json.Json
import me.jakejmattson.discordkt.api.annotations.Service

@kotlinx.serialization.ImplicitReflectionSerializer
@kotlinx.serialization.UnstableDefault
@Service
class WikipediaSummaryService {
    fun getSummary(topic: String): WikipediaSummary? {

        val result = Fuel.get("https://en.wikipedia.org/api/rest_v1/page/summary/$topic").responseString()

        if (result.third.component2() != null) {
            return null
        }

        return Json { ignoreUnknownKeys = true }.parse(WikipediaSummary.serializer(), result.third.get())
    }
}