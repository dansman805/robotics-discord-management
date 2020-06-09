package com.github.dansman805.discordbot.services

import com.github.dansman805.discordbot.botConfig
import com.github.dansman805.discordbot.db
import com.github.dansman805.discordbot.entities.Messages
import com.github.dansman805.discordbot.toDate
import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.*
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import java.io.File
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.floor
import org.knowm.xchart.*
import org.knowm.xchart.internal.chartpart.Chart
import org.knowm.xchart.internal.series.Series
import org.knowm.xchart.style.Styler
import java.awt.Color

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

    private fun messagesPerDay(user: User?, guild: Guild, days: Int): SortedMap<Long, Int> {

        val sequence = if (user == null) {
            sequence
                    .sortedBy { it.epochSecond }
                    .asKotlinSequence()
        }
        else {
            sequence
                    .sortedBy { it.epochSecond }
                    .filter { it.guildId eq guild.idLong }
                    .filter { it.authorId eq user.idLong }
                    .asKotlinSequence()
        }

        val instanceOfDays = HashMap<Long, Int>()

        sequence.map { epochSecondToTimeSpanEpochSecond(it.epochSecond, days) }.forEach {
            instanceOfDays[it] = instanceOfDays[it]?.plus(1) ?: 1
        }

        return instanceOfDays.toSortedMap()
    }

    fun cumulativeMessages(user: User?, guild: Guild, textChannel: TextChannel, days: Int, svg: Boolean) {
        val messagesPerDay = messagesPerDay(user, guild, days).map { Pair<Long, Int>(it.key, it.value) }

        val cumulativeMessagesPerDay = messagesPerDay.toMutableList()

        val dates = mutableListOf<Date>()
        val messages = mutableListOf<Int>()

        for (i in 0 until messagesPerDay.count()) {
            val messageCountForDay = cumulativeMessagesPerDay[i]

            dates.add(messageCountForDay.first.toDate())
            messages.add(messageCountForDay.second + if (i > 0) messages[i - 1] else 0)
        }

        val plot = XYChartBuilder().apply {
            title = title("Cumulative Messages", guild, user)
        }
                .xAxisTitle("Time")
                .yAxisTitle("Messages")
                .build()

        plot.addSeries("Messages", dates, messages)
        plot.styleAndSend(textChannel, svg)
    }

    fun messageRanking(guild: Guild, textChannel: TextChannel, topN: Int, svg: Boolean) {
        val messages = sequence.filter { it.guildId eq guild.idLong }
                .asKotlinSequence()
        val messageCounts = HashMap<Long, Int>()

        messages.map { it.authorId }.forEach {
            messageCounts[it] = messageCounts[it]?.plus(1) ?: 1
        }

        val topNPeople = messageCounts.toList().sortedByDescending { it.second }.subList(0, topN)

        val users = topNPeople.map { guild.jda.getUserById(it.first)?.name ?: it.first.toString() }
        val topNMessageCounts = topNPeople.map { it.second }

        val plot = CategoryChartBuilder().apply {
            title = title("Message Authors", guild)
        }
                .xAxisTitle("Author")
                .yAxisTitle("Message Count")
                .build()

        plot.addSeries("Messages", users, topNMessageCounts)
        plot.styleAndSend(textChannel, svg)
    }

    fun messages(user: User?, guild: Guild, textChannel: TextChannel, days: Int, svg: Boolean) {
        val messagesPerDay = messagesPerDay(user, guild, days).toList()

        val dates = messagesPerDay.map { it.first.toDate() }
        val messages = messagesPerDay.map { it.second / days }

        // val dateTimeFormatter = DateTimeFormatter.ISO_DATE

        val plot = XYChartBuilder().apply {
            title = title("Messages", guild, user)
        }
                .xAxisTitle("Time")
                .yAxisTitle("Messages / Day")
                .build()

        plot.addSeries("Messages / Day", dates, messages)
        plot.styleAndSend(textChannel, svg)
    }

    fun hourlyMessages(user: User?, guild: Guild, textChannel: TextChannel, svg: Boolean) {
        val sequence = if (user == null) {
            sequence
                    .sortedBy { it.epochSecond }
                    .filter { it.guildId eq guild.idLong }
                    .asKotlinSequence()
        }
        else {
            sequence
                    .sortedBy { it.epochSecond }
                    .filter { it.guildId eq guild.idLong }
                    .filter { it.authorId eq user.idLong }
                    .asKotlinSequence()
        }

        val hours = IntArray(24)
        val calendar = Calendar.getInstance()

        sequence.forEach {
            calendar.time = it.epochSecond.toDate()

            hours[calendar.get(Calendar.HOUR_OF_DAY)] += 1
        }

        val plot = CategoryChartBuilder().apply {
            title = title("Message Time", guild, user)

        }
                .xAxisTitle("Hour of Day")
                .yAxisTitle("Messages")
                .build()

        plot.addSeries("Messages", hours.indices.asSequence().toList().toIntArray(), hours)
        plot.styleAndSend(textChannel, svg, false)
    }

    fun channelDistribution(user: User?, guild: Guild, textChannel: TextChannel, svg: Boolean) {
        val messages = if (user == null) {
            sequence.filter { it.guildId eq guild.idLong }

        }
        else {
            sequence.filter { it.guildId eq guild.idLong }.filter { it.authorId eq user.idLong }
        }

        val channelCounts = HashMap<Long, Int>()

        messages.map { it.channelId }.forEach {
            channelCounts[it] = channelCounts[it]?.plus(1) ?: 1
        }

        val sortedChannelCounts = channelCounts.toList()
                .sortedByDescending { it.second }

        val channelNames = sortedChannelCounts.map { guild.getTextChannelById(it.first)?.name ?: it.first.toString() }
        val channelMessageCounts = sortedChannelCounts.map { it.second }

        val plot = CategoryChartBuilder().apply {
            title = title("Channel Distribution", guild, user)
        }
                .xAxisTitle("Channel")
                .yAxisTitle("Messages")
                .build()

        plot.addSeries("Messages", channelNames, channelMessageCounts)
        plot.styleAndSend(textChannel, svg)
    }

    private fun CategoryChart.styleAndSend(textChannel: TextChannel, svg: Boolean, showLegend: Boolean=true) {
        this.styler.apply {
            xAxisLabelRotation = -90
            xAxisLabelAlignmentVertical = Styler.TextAlignment.Right
            legendPosition = Styler.LegendPosition.InsideNE
            isLegendVisible = showLegend
        }

        styleAndSend(this, textChannel, svg)
    }

    private fun XYChart.styleAndSend(textChannel: TextChannel, svg: Boolean) {
        this.styler.apply {
            markerSize = 5
            legendPosition = Styler.LegendPosition.InsideNW
        }

        styleAndSend(this, textChannel, svg)
    }

    private fun <T: Styler, U: Series> styleAndSend(chart: Chart<T, U>, textChannel: TextChannel, svg: Boolean) {
        val location = botConfig.dateTimeFormatter.format(LocalDateTime.now()) + if (svg) {".svg"} else { ".png" }

        chart.styler.apply {
            chartBackgroundColor = Color.WHITE //(214, 214, 214)
            plotBackgroundColor = Color.WHITE
            decimalPattern = "#,###,###,###,###"
        }

        if (svg) {
            VectorGraphicsEncoder.saveVectorGraphic(chart,
                    location,
                    VectorGraphicsEncoder.VectorGraphicsFormat.SVG)
        }
        else {
            BitmapEncoder.saveBitmap(chart, location, BitmapEncoder.BitmapFormat.PNG)
        }

        val file = File(location)

        textChannel.sendFile(file).complete()

        Thread.sleep(1000)

        file.delete()
    }

    private fun title(title: String, guild: Guild, user: User?=null): String {
        val of = user?.name ?: guild.name

        return "$title of $of"
    }
}