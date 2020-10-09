package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.botConfig
import com.github.dansman805.discordbot.db
import com.github.dansman805.discordbot.entities.MessageDatabaseEntry
import com.github.dansman805.discordbot.entities.Messages
import com.github.dansman805.discordbot.extensions.CODE_BLOCK_DELIMITER
import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.entity.channel.TextChannel
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.*
import me.jakejmattson.discordkt.api.annotations
import me.jakejmattson.discordkt.api.dsl.*

const val developerCategoryName = "Developer"

class IsDeveloper : Precondition() {
    override suspend fun evaluate(event: CommandEvent<*>): PreconditionResult {
        if (event.command!!.category != developerCategoryName) {
            return Pass
        }

        if (botConfig.developerIds.contains(event.author.id.longValue)) {
            return Pass
        } else {
            return Fail("You must be a developer to run this command!")
        }
    }
}

fun developerCommands(scriptEngineService: ScriptEngineService) = commands("developerCategoryName") {
    fun getLatestMessage(channel: TextChannel): Snowflake = try {
        channel.lastMessageId!!
    }
    catch (e: Exception) {
        getLatestMessage(channel)
    }

    command("Refresh") {
        execute {

                val messages = db.sequenceOf(Messages)

                for (channel in guild!!.textChannels) {
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

    command("Eval") {
        execute(EveryArg) {
            it.safe(false) {
                context = it.discordContext

                val result = scriptEngineService.evaluateScript(
                        """
                import com.github.dansman805.discordbot.commands.context
                import com.github.dansman805.discordbot.extensions.properties
                import net.dv8tion.jda.*
                import me.jakejmattson.kutils.api.extensions.*
                """.trimIndent() + "\n" + it.args.first.replace("`", "")
                )
                it.respond(CODE_BLOCK_DELIMITER
                        + (result ?: "Done").toString().replace("`", "")
                        + CODE_BLOCK_DELIMITER)
            }
        }
    }
}

lateinit var context: DiscordContext