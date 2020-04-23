package com.github.dansman805.discordbot.services

import com.github.dansman805.discordbot.botConfig
import com.github.dansman805.discordbot.db
import com.github.dansman805.discordbot.entities.Messages
import kravis.*
import me.aberrantfox.kjdautils.api.annotation.Service
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.*
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import java.awt.Dimension
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.floor

@Service
class StatisticsService {
    private val sequence = if (botConfig.hiddenChannelIds == null) {
        db.sequenceOf(Messages)
    }
    else {
        db.sequenceOf(Messages).filter { it.channelId notInList botConfig.hiddenChannelIds!! }
    }

    private fun secondsInTimeSpan(days: Int) = 24 * 60 * 60 * days

    private fun epochSecondToTimeSpanEpochSecond(epochSecond: Long, days: Int) =
            (floor(epochSecond/secondsInTimeSpan(days).toDouble()) * secondsInTimeSpan(days)).toLong()

    private fun messagesPerDay(user: User?, guild: Guild, filter: String, days: Int): SortedMap<Long, Int> {
        val pattern = filter.toRegex(RegexOption.IGNORE_CASE)

        val sequence = if (user == null) {
            sequence
                    .sortedBy { it.epochSecond }
                    .filter { it.guildId eq guild.idLong }
                    .asKotlinSequence()
                    .filter { pattern.containsMatchIn(it.contentRaw) }
        }
        else {
            sequence
                    .sortedBy { it.epochSecond }
                    .filter { it.guildId eq guild.idLong }
                    .filter { it.authorId eq user.idLong }
                    .asKotlinSequence()
                    .filter { pattern.containsMatchIn(it.contentRaw) }
        }

        val instanceOfDays = HashMap<Long, Int>()

        sequence.map { epochSecondToTimeSpanEpochSecond(it.epochSecond, days) }.forEach {
            instanceOfDays[it] = instanceOfDays[it]?.plus(1) ?: 1
        }

        return instanceOfDays.toSortedMap()
    }

    fun cumulativeMessages(user: User?, guild: Guild, textChannel: TextChannel, filter: String, days: Int, svg: Boolean) {
        val messagesPerDay = messagesPerDay(user, guild, filter, days).map { Pair<Long, Int>(it.key, it.value) }

        val cumulativeMessagesPerDay = messagesPerDay.toMutableList()

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
                .title("Cumulative Messages", guild, user, filter)
                .xLabel("Time")
                .yLabel("Messages")
                .styleAndSend(textChannel, svg)

        textChannel.sendFile(file).complete()
    }

    fun messageRanking(guild: Guild, textChannel: TextChannel, filter: String, topN: Int, svg: Boolean) {
        val pattern = filter.toRegex(RegexOption.IGNORE_CASE)
        val messages = sequence.filter { it.guildId eq guild.idLong }
                .asKotlinSequence()
                .filter { pattern.containsMatchIn(it.contentRaw) }

        val messageCount = HashMap<Long, Int>()
        messages.forEach {
            messageCount[it.authorId] = messageCount[it.authorId]?.plus(1) ?: 1
        }
        
        val topNMessageUsers = messageCount
                .toList()
                .sortedByDescending { it.second }
                .subList(0, topN)
                .map { Pair(it.first, guild.jda.getUserById(it.first)?.name ?: "???") }

        val topNMessageUserIds = topNMessageUsers.map { it.first }.toList()

        messages.asIterable()
                .filter { it.authorId in topNMessageUserIds }
                .plot(x = {it.authorId}, y = {it})
                .geomBar(stat = Stat.count)
                .title("Top Users by Messages", guild, filter = filter)
                .xLabel("User")
                .yLabel("Message Count")
                .styleAndSend(textChannel, svg)
    }

    fun messages(user: User?, guild: Guild, textChannel: TextChannel, filter: String, days: Int, svg: Boolean) {
        val messagesPerDay = messagesPerDay(user, guild, filter, days).map { Pair<Long, Int>(it.key, it.value) }
        val dateTimeFormatter = DateTimeFormatter.ISO_DATE

        val file = File("${botConfig.dateTimeFormatter.format(LocalDateTime.now())}.png")

        messagesPerDay.plot(
                x = { LocalDateTime.ofEpochSecond(it.first, 0, ZoneOffset.UTC).format(dateTimeFormatter) },
                y = {it.second / days})
                .geomLine()
                .title("Messages", guild, user, filter)
                .xLabel("Time")
                .yLabel("Messages")
                .styleAndSend(textChannel, svg)
    }

    fun channelDistribution(user: User?, guild: Guild, textChannel: TextChannel, svg: Boolean) {
        val messages = if (user == null) {
            sequence
        }
        else {
            sequence.filter { it.authorId eq user.idLong }
        }

        val channelCounts = HashMap<Long, Int>()

        messages.map { it.channelId }.forEach {
            channelCounts[it] = channelCounts[it]?.plus(1) ?: 1
        }

        channelCounts
                .map { Pair<String, Int>(guild.getTextChannelById(it.key)!!.name, it.value) }
                .filter { it.second > 10 }
                .plot(x = { it.first }, y = {it.second})
                .geomCol()
                .title("Channel Distribution", guild, user)
                .xLabel("Channel")
                .yLabel("Message Count")
                .coordFlip()
                .styleAndSend(textChannel, svg)
    }

    private fun GGPlot.styleAndSend(textChannel: TextChannel, svg: Boolean) {
        val file = File("${botConfig.dateTimeFormatter.format(LocalDateTime.now())}.${if (svg) "svg" else "png"}")

        this.themeBW()
                .save(file, Dimension(1920, 1080))

        textChannel.sendFile(file).complete()

        file.delete()
    }

    private fun GGPlot.title(title: String, guild: Guild, user: User?=null, filter: String=""): GGPlot {
        val of = user?.name ?: guild.name
        val containing = if (filter == "") "" else  " containing $filter"

        return this.title("$title of $of$containing")
    }
}