package com.github.dansman805.discordbot.listeners

import com.github.dansman805.discordbot.botConfig
import com.github.dansman805.discordbot.extensions.thumbUpDown
import com.google.common.eventbus.Subscribe
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class PublicIdeaListener {
    @Subscribe
    fun onMessageReceived(event: MessageReceivedEvent) {
        if (botConfig.voteChannelIds?.contains(event.channel.idLong) == true) {
            event.message.thumbUpDown()
        }
    }
}