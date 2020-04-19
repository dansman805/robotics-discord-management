package com.github.dansman805.discordbot.listeners

import com.github.dansman805.discordbot.botConfig
import com.google.common.eventbus.Subscribe
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class PublicIdeaListener {
    @Subscribe
    fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.channel.idLong == botConfig.publicIdeaChannelId) {
            event.message.addReaction("U+1F44D").complete() // thumbs up
            event.message.addReaction("U+1F44E").complete() // thumbs down
        }
    }
}