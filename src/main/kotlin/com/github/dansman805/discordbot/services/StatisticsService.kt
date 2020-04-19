package com.github.dansman805.discordbot.services

import com.github.dansman805.discordbot.botConfig
import com.github.dansman805.discordbot.db
import com.github.dansman805.discordbot.entities.Messages
import kravis.geomLine
import kravis.plot
import kravis.theme
import me.aberrantfox.kjdautils.api.annotation.Service
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.*
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.floor

@Service
class StatisticsService {
    private val secondsInDay = 24 * 60 * 60

    private fun epochSecondToDayEpochSecond(epochSecond: Long) =
            (floor(epochSecond/secondsInDay.toDouble()) * secondsInDay).toLong()

    private fun messagesPerDay(user: User?, guild: Guild): SortedMap<Long, Int> {
        val sequence = if (user == null) {
                db.sequenceOf(Messages)
                        .sortedBy { it.epochSecond }
                        .filter { it.guildId eq guild.idLong }
        }
        else {
            db.sequenceOf(Messages)
                    .sortedBy { it.epochSecond }
                    .filter { it.guildId eq guild.idLong }
                    .filter { it.authorId eq user.idLong }
        }

        val instanceOfDays = HashMap<Long, Int>()

        sequence.map { epochSecondToDayEpochSecond(it.epochSecond) }.forEach {
            instanceOfDays[it] = instanceOfDays[it]?.plus(1) ?: 1
        }

        return instanceOfDays.toSortedMap()
    }

    fun cumulativeMessages(user: User?, guild: Guild, textChannel: TextChannel, shouldYBeLog: Boolean) {
        val messagesPerDay = messagesPerDay(user, guild).map { Pair<Long, Int>(it.key, it.value) }

        var cumulativeMessagesPerDay = messagesPerDay.toMutableList()

        for (i in 0 until cumulativeMessagesPerDay.count()) {
            val messageCountForDay = cumulativeMessagesPerDay[i]

            cumulativeMessagesPerDay[i] = Pair<Long, Int>(
                    messageCountForDay.first,
                    messageCountForDay.second + if (i > 0) cumulativeMessagesPerDay[i - 1].second else 0
            )
        }

        val dateTimeFormatter = DateTimeFormatter.ISO_DATE

        val file = File("${botConfig.dateTimeFormatter.format(LocalDateTime.now())}.png")

        cumulativeMessagesPerDay.plot(x = {
            LocalDateTime.ofEpochSecond(it.first, 0, ZoneOffset.UTC).format(dateTimeFormatter)
        }, y = {it.second})
                .geomLine()
                .theme()
                .title("Cumulative Messages of ${user?.name ?: guild.name}")
                .xLabel("Time")
                .yLabel("Messages")
                .save(file)

        textChannel.sendFile(file).complete()

        file.delete()
    }
}