package com.github.dansman805

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
        MembershipTimeRole("Rookie Member", Color(0xEEEEEE), 0),
        MembershipTimeRole("Aspiring Roboticist", Color(0x4cacff), 30),
        MembershipTimeRole("Robot Enthusiast", Color(0xfb1414), 90),
        MembershipTimeRole("Veteran Member", Color(0x099409), 365),
        MembershipTimeRole("Gracious Professional", Color(0x9091f6), 365 * 2),
        MembershipTimeRole("Certified Boomer", Color(0xff8c00), 365 * 3)
)