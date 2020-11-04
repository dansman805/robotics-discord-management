package com.github.dansman805.discordbot.entities

import com.gitlab.kordlib.core.entity.Message
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.*

interface MessageDatabaseEntry : Entity<MessageDatabaseEntry> {
    var messageId: Long
    var guildId: Long?
    var channelId: Long
    var authorId: Long
    var epochSecond: Long
    var contentRaw: String

    companion object {
        suspend fun fromMessage(m: Message) = Entity.create<MessageDatabaseEntry>().apply {
            messageId = m.id.longValue
            guildId = m.getGuild().id.longValue
            channelId = m.channel.id.longValue
            authorId = m.author!!.id.longValue
            epochSecond = m.timestamp.epochSecond
            contentRaw = ""
        }
    }
}

object Messages : Table<MessageDatabaseEntry>("messages") {
    val messageId by long("message_id").primaryKey().bindTo { it.messageId }
    val guildId by long("guild_id").bindTo { it.guildId }
    val channelId by long("channel_id").bindTo { it.channelId }
    val authorId by long("author_id").bindTo { it.authorId }
    val epochSecond by long("epoch_second").bindTo { it.epochSecond }
    val contentRaw by text("content_raw").bindTo { it.contentRaw }
}