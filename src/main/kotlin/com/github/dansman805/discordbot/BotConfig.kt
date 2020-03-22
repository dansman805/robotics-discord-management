package com.github.dansman805.discordbot

import com.github.dansman805.discordbot.dataclasses.MembershipTimeRole
import java.awt.Color
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

val memberShipRoles = listOf<MembershipTimeRole>(
        MembershipTimeRole("New Member", Color(0x12345), 1),
        MembershipTimeRole("Member", Color(0x23456), 30),
        MembershipTimeRole("Old Member", Color(0x34560), 60),
        MembershipTimeRole("Older Member", Color(0x456789), 90),
        MembershipTimeRole("Oldest Member", Color(0x567890), 365)
        )