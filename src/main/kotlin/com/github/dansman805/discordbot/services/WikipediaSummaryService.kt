package com.github.dansman805.discordbot.services

import com.github.dansman805.discordbot.dataclasses.WikipediaSummary
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.HttpException
import com.github.kittinunf.fuel.core.response
import com.github.kittinunf.fuel.serialization.responseObject
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.parse
import me.aberrantfox.kjdautils.api.annotation.Service

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