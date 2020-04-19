package com.github.dansman805.discordbot.entities

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.*
import net.dv8tion.jda.api.entities.Message

interface MessageDatabaseEntry : Entity<MessageDatabaseEntry> {
    var messageId: Long
    var guildId: Long?
    var channelId: Long
    var authorId: Long
    var contentRaw: String
    var epochSecond: Long

    companion object {
        fun fromMessage(m: Message) = Entity.create<MessageDatabaseEntry>().apply {
            messageId = m.idLong
            guildId = m.guild.idLong
            channelId = m.channel.idLong
            authorId = m.author.idLong
            contentRaw = m.contentRaw
            epochSecond = m.timeCreated.toEpochSecond()
        }
    }
}

object Messages : Table<MessageDatabaseEntry>("messages") {
    val messageId by long("message_id").primaryKey().bindTo { it.messageId }
    val guildId by long("guild_id").bindTo { it.guildId }
    val channelId by long("channel_id").bindTo { it.channelId }
    val authorId by long("author_id").bindTo { it.authorId }
    val contentRaw by varchar("content_raw").bindTo { it.contentRaw }
    val epochSecond by long("epoch_second").bindTo { it.epochSecond }
}