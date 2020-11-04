package com.github.dansman805.discordbot

import com.github.dansman805.discordbot.extensions.thumbUpDown
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.emoji.Emojis
import me.jakejmattson.discordkt.api.dsl.bot
import me.jakejmattson.discordkt.api.dsl.listeners
import java.awt.Color

suspend fun main() {
    initDb()

    bot(botConfig.discordToken) {
        prefix {
            "+"
        }

        configure {
            //Allow a mention to be used in front of commands ('@Bot help`).
            allowMentionPrefix = true

            //Whether or not to generate documentation for registered commands.
            generateCommandDocs = true

            //Whether or not to show registered entity information on startup.
            showStartupLog = true

            //An emoji added when a command is received ('null' to disable).
            commandReaction = Emojis.eyes

            //A color constant for your bot - typically used in embeds.
            theme = Color(0x00BFFF)
        }

        permissions {
            true
        }

        //The Discord presence shown on your bot.
        presence {
            this.listening("dansman805's problems")
        }

        listeners {
            on<MessageCreateEvent> {
                if (this.message.channelId == botConfig.publicIdeaChannelId) {
                    this.message.thumbUpDown()
                }
            }
        }
    }
}

