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
    val databaseLocation: String,
    val orangeAllianceKey: String,
    val blueAllianceKey: String,
    val appName: String,
    val modLogChannelID : Long,
    val membershipRoles: List<MembershipTimeRole>,
    val joinedLogID: Long,
    val developerIDs: List<Long>,
    val publicIdeaChannelID: Long? = null,
    val dateTimeFormatPattern: String = "u-M-d H:m:s.S"
) {
    val dateTimeFormatter: DateTimeFormatter get() = DateTimeFormatter.ofPattern(dateTimeFormatPattern)
    private val databaseFullPath get() = File(databaseLocation).absolutePath
    val databaseURL get() = "jdbc:sqlite:$databaseFullPath"
}

private val json = Json(JsonConfiguration.Stable)
private val configFile = File("config.json")

val botConfig by lazy {
    try {
        val serializer = BotConfig.serializer()
        json.parse(serializer, configFile.readText())
    }
    catch (e: FileNotFoundException) {
        throw FileNotFoundException("Cannot find config.json! It should be located at ${File(".").canonicalPath}")
    }
}


fun initDb() {

}
