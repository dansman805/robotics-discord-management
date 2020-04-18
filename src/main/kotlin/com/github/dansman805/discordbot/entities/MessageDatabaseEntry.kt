package com.github.dansman805.discordbot.entities

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.*

interface MessageDatabaseEntry : Entity<MessageDatabaseEntry> {
    companion object : Entity.Factory<MessageDatabaseEntry>()
    val id: Long
    var messageId: Long
    var guildId: Long
    var channelId: Long
    var authorId: Long
    var contentRaw: String
}

object Messages : Table<MessageDatabaseEntry>("messages") {
    val id by long("id").primaryKey().bindTo { it.id }
    val messageId by long("message_id").bindTo { it.messageId }
    val guildId by long("guild_id").bindTo { it.guildId }
    val channelId by long("channel_id").bindTo { it.channelId }
    val authorId by long("author_id").bindTo { it.authorId }
    val contentRaw by varchar("content_raw").bindTo { it.contentRaw }
}