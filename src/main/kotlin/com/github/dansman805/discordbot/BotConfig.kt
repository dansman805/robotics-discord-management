package com.github.dansman805.discordbot

import java.time.format.DateTimeFormatter

fun readEnvVariable(envVariable: String) =
        System.getenv(envVariable) ?: throw Exception("No $envVariable environment variable found")

val discordToken: String = readEnvVariable("DISCORD_TOKEN")

val orangeAllianceKey: String = readEnvVariable("TOA_KEY")
val blueAllianceKey: String = readEnvVariable("TBA_KEY")

val appName: String = readEnvVariable("BOT_NAME")
val modLogChannelID: Long = readEnvVariable("MOD_LOG_CHANNEL_ID").toLong()
val editDeleteChannelID: Long = readEnvVariable("EDIT_DELETE_CHANNEL_ID").toLong()


val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("u-M-d H:m:s.S")