package com.github.dansman805.discordbot.extensions

// shamelessly stolen from
// https://github.com/JakeJMattson/RoleBot/blob/master/src/main/kotlin/me/jakejmattson/rolebot/extensions/ColorExtensions.kt

import java.awt.Color

fun Color.toHexString() = "#%02X%02X%02X".formatArray(intArrayOf(red, green, blue))

private fun String.formatArray(data: IntArray) = String.format(this, *data.map { it as Int? }.toTypedArray())