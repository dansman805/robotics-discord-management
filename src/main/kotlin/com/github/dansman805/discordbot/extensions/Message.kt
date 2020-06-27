package com.github.dansman805.discordbot.extensions

import net.dv8tion.jda.api.entities.Message

fun Message.thumbUpDown() {
    this.removeReaction("\uD83D\uDC40").submit() // eyes

    this.addReaction("U+1F44D").submit() // thumbs up
    this.addReaction("U+1F44E").submit() // thumbs down
}