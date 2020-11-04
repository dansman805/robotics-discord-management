package com.github.dansman805.discordbot

import java.awt.Color
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

fun Color.toRGB() = "R: ${this.red}, G: ${this.green}, B: ${this.blue}"

fun Long.toDate(): Date = Date.from(Instant.ofEpochSecond(this))

fun Long.toDateTime(): OffsetDateTime = Instant.ofEpochSecond(this).atOffset(ZoneOffset.UTC)