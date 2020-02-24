package com.github.dansman805.discordbot.dataclasses

import kotlinx.serialization.Serializable
import me.aberrantfox.kjdautils.api.dsl.embed

@Serializable
data class WikipediaSummary(val title: String, val extract: String, val thumbnail: Thumbnail? = null) {
    val thumbnailUrl = thumbnail?.source

    fun toEmbed() = embed {
        title = "Wikipedia: ${this@WikipediaSummary.title}"

        image = thumbnailUrl

        field {
            name = "Summary"
            value = extract
        }
    }
}

@Serializable
data class Thumbnail(val source: String)
