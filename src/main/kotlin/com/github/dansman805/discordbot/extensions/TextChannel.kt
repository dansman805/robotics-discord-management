package com.github.dansman805.discordbot.extensions

fun TextChannel.allMessages(): List<Message> {
    // Get the first up to 100 messages in order to start off the list
    val messages =
            this.getHistoryFromBeginning(100).complete().retrievedHistory.sortedBy { it.timeCreated }.toMutableList()

    // Gets the last message of the server so we can see when all the messages are read

    var lastMessagesCount = messages.size

    // Loops until the last message is in the list
    while (messages.none { it.timeCreated.isBefore(messages.first().timeCreated) }) {
        // Adds the next up to 100 messages
        messages.addAll(
                this.getHistoryAfter(messages.last().idLong, 100).complete().retrievedHistory.sortedBy { it.timeCreated }
        )

        println("${messages.count()}, ${messages.last().contentDisplay}")

        if (messages.size == lastMessagesCount) {
            break
        }

        lastMessagesCount = messages.size
    }

    // Uses distinctBy because there can be up to 99 repeated messages because getHistory loops around
    return messages.distinctBy { it.idLong }.sortedBy { it.timeCreated }
}