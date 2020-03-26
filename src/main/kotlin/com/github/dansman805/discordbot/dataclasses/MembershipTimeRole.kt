package com.github.dansman805.discordbot.dataclasses

import kotlinx.serialization.Serializable
import java.awt.Color

@Serializable
data class MembershipTimeRole(val name: String,
                              val r: Int,
                              val g: Int,
                              val b: Int,
                              val requiredTimeInDays: Int) {
    val color get() = Color(r, g, b)
}