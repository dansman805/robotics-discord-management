package com.github.dansman805.discordbot.extensions

import com.gitlab.kordlib.core.entity.Message
import com.gitlab.kordlib.kordx.emoji.Emojis
import com.gitlab.kordlib.kordx.emoji.addReaction
import com.gitlab.kordlib.kordx.emoji.deleteOwnReaction

suspend fun Message.thumbUpDown() {
    this.deleteOwnReaction(Emojis.eyes)

    this.addReaction(Emojis.thumbsup) // thumbs up
    this.addReaction(Emojis.thumbsdown) // thumbs down
}