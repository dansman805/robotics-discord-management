package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.botConfig
import com.github.dansman805.discordbot.db
import com.github.dansman805.discordbot.entities.MessageDatabaseEntry
import com.github.dansman805.discordbot.entities.Messages
import com.github.dansman805.discordbot.extensions.safe
import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.annotation.Precondition
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.aberrantfox.kjdautils.internal.command.Fail
import me.aberrantfox.kjdautils.internal.command.Pass
import me.aberrantfox.kjdautils.internal.command.precondition
import me.aberrantfox.kjdautils.internal.services.ConversationService
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.*
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.TextChannel

const val developerCategoryName = "Developer"

@Precondition
fun isDeveloper() = precondition {
    if (it.container[it.commandStruct.commandName]?.category != developerCategoryName) {
        return@precondition Pass
    }

    if (botConfig.developerIds.contains(it.author.idLong)) {
        return@precondition Pass
    } else {
        return@precondition Fail("You must be a developer to run this command!")
    }
}

@CommandSet(developerCategoryName)
fun developerCommands(conversationService: ConversationService) = commands {
    fun getLatestMessage(channel: TextChannel): Long  = try {
        channel.latestMessageIdLong
    }
    catch (e: Exception) {
        getLatestMessage(channel)
    }

    command("Refresh") {
        execute {
            it.safe {
                val messages = db.sequenceOf(Messages)

                for (channel in it.guild!!.textChannels) {
                    println("Started: ${channel.name}")

                    if (channel.jda
                                    .selfUser
                                    .toMember(it.guild!!)!!
                                    .hasPermission(channel, Permission.MESSAGE_HISTORY)
                    ) {
                        var latestKnownMessage = messages
                                .filter { it.channelId eq channel.idLong }
                                .sortedBy { it.epochSecond }
                                .lastOrNull()?.messageId
                        var messagesProcessed = 0

                        while (true) {
                            val next100 = if (latestKnownMessage == null) {
                                channel.getHistoryFromBeginning(100).complete().retrievedHistory
                            } else {
                                channel.getHistoryAfter(latestKnownMessage, 100).complete().retrievedHistory
                            }

                            next100.forEach {
                                messages.add(MessageDatabaseEntry.fromMessage(it))
                            }

                            val latestMessage = getLatestMessage(channel)

                            if (messages.any { it.messageId eq latestMessage}) {
                                break
                            }

                            messagesProcessed += next100.count()

                            if (next100.size > 0) {
                                println("$messagesProcessed, ${botConfig.dateTimeFormatter.format(next100.first().timeCreated)}")
                                latestKnownMessage = next100.maxBy { it.timeCreated.toEpochSecond() }!!.idLong
                            }
                            else {
                                break
                            }
                        }

                        println("Finished: ${channel.name}; ${messages.filter { it.channelId eq channel.idLong }.count()} messages")
                    }
                    else {
                        println("Skipped: ${channel.name} due to lack of permission")
                    }
                }

                it.respond("Done.")
            }
        }
    }

    command("DBMessageCount") {
        execute {
            it.safe {
                val sequence = db.sequenceOf(Messages)

                it.respond("Number of Messages Stored: ${sequence.totalRecords}")
            }
        }
    }
}
