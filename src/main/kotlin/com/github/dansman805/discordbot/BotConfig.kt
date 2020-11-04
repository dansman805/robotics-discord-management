package com.github.dansman805.discordbot

import com.github.dansman805.discordbot.dataclasses.MembershipTimeRole
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.logging.ConsoleLogger
import me.liuwj.ktorm.logging.LogLevel
import java.io.File
import java.io.FileNotFoundException
import java.time.format.DateTimeFormatter

@Serializable
data class BotConfig(
        val discordToken: String,
        val databaseURL: String,
        val orangeAllianceKey: String,
        val blueAllianceKey: String,
        val appName: String,
        val modLogChannelId : Long,
        val membershipRoles: List<MembershipTimeRole>,
        val joinedLogId: Long,
        val developerIds: List<Long>,
        val hiddenChannelIds: List<Long>? = null,
        val voteChannelIds: List<Long>? = null,
        val dateTimeFormatPattern: String = "u-M-d H:m:s.S"
) {
    val dateTimeFormatter: DateTimeFormatter get() = DateTimeFormatter.ofPattern(dateTimeFormatPattern)
}

private val json = Json
private val configFile = File("config.json")

val botConfig by lazy {
    try {
        val serializer = BotConfig.serializer()
        Json.parse(serializer, configFile.readText())
    }
    catch (e: FileNotFoundException) {
        throw FileNotFoundException("Cannot find config.json! It should be located at ${File(".").canonicalPath}")
    }
}

val db by lazy {
    Database.connect (
        url = botConfig.databaseURL,
            driver = "org.sqlite.JDBC",
            logger = ConsoleLogger(threshold = LogLevel.INFO)
    )
}

fun initDb() {
    db
}
