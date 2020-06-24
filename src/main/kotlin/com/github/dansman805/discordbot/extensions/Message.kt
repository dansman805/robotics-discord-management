package com.github.dansman805.discordbot.extensions

import net.dv8tion.jda.api.entities.Message

fun Message.thumbUpDown() {
    val stowonks = this.guild.retrieveEmotes().complete().firstOrNull { it.name == "stowonks" }
    val notStowonks = this.guild.retrieveEmotes().complete().firstOrNull { it.name == "notstowonks" }

    this.removeReaction("\uD83D\uDC40").submit() // eyes

    if (stowonks != null && notStowonks != null) {
        this.addReaction(stowonks).submit()
        this.addReaction(notStowonks).submit()
    }
    else {
        this.addReaction("U+1F44D").submit() // thumbs up
        this.addReaction("U+1F44E").submit() // thumbs down
    }
}