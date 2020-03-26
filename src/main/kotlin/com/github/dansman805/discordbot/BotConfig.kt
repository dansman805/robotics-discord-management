package com.github.dansman805.discordbot

import com.github.dansman805.discordbot.dataclasses.MembershipTimeRole
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.File
import java.io.FileNotFoundException
import java.time.format.DateTimeFormatter

@Serializable
data class BotConfig(
    val discordToken: String,
    val orangeAllianceKey: String,
    val blueAllianceKey: String,
    val appName: String,
    val modLogChannelID : Long,
    val membershipRoles: List<MembershipTimeRole>,
    val dateTimeFormatPattern: String = "u-M-d H:m:s.S"
) {
    val dateTimeFormatter get() = DateTimeFormatter.ofPattern(dateTimeFormatPattern)
}

val json = Json(JsonConfiguration.Stable.copy(unquoted = true))
val configFile = File("config.json")

val botConfig by lazy {
    try {
        json.parse(BotConfig.serializer(), configFile.readText())
    }
    catch (e: FileNotFoundException) {
        throw FileNotFoundException("Cannot find config.json! It should be located at ${File(".").canonicalPath}")
    }
}

/*val memberShipRoles = listOf<MembershipTimeRole>(
        MembershipTimeRole("Rookie Member", Color(0xEEEEEE), 0),
        MembershipTimeRole("Aspiring Roboticist", Color(0x4cacff), 30),
        MembershipTimeRole("Robot Enthusiast", Color(0xfb1414), 90),
        MembershipTimeRole("Veteran Member", Color(0x099409), 365),
        MembershipTimeRole("Gracious Professional", Color(0x9091f6), 365 * 2),
        MembershipTimeRole("Certified Boomer", Color(0xff8c00), 365 * 3)
)*/