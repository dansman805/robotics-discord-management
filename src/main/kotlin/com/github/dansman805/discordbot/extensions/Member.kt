package com.github.dansman805.discordbot.extensions

import com.github.dansman805.discordbot.db
import com.github.dansman805.discordbot.entities.Messages
import com.github.dansman805.discordbot.toDateTime
import com.gitlab.kordlib.common.entity.Permission
import com.gitlab.kordlib.core.behavior.channel.createEmbed
import java.time.OffsetDateTime
import com.gitlab.kordlib.core.entity.Member
import com.gitlab.kordlib.core.entity.Message
import com.gitlab.kordlib.core.entity.channel.MessageChannel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.*

suspend fun Member.sendEmbed(channel: MessageChannel) {
    channel.createEmbed {
        val m = this@sendEmbed

        color = m.roles.first().color

        thumbnail {
            url = m.asUser().avatar.url
        }

        field {
            name = "Name"
            value = m.displayName
        }

        field {
            name = "ID"
            value = m.id.value
        }

        field {
            name = "Nickname"
            value = m.nickname ?: "n/a"
            inline = m.nickname == null
        }

        field {
            name = (if (m.isBot == true) "Bot Created" else "User Joined Discord") + "(UTC)"
            value = m.asUser().id.formattedTimeStamp
        }

        field {
            name = "Joined Guild"
            value = m.id.formattedTimeStamp
        }

        field {
            name = "Color"
            value = m.roles.first().color.toHexString()
        }


        field {
            name = "Roles"
            value = m.roles.toList().joinToString(", ")
        }

        field {
            name = "Icon URL"
            value = m.asUser().avatar.url
        }
    }
}

private val messageSequence by lazy { db.sequenceOf(Messages) }

fun Member.firstJoin(joinLogs: List<Message>): OffsetDateTime {
    return minOf(
            minOf(
                    this.id.timeStampUTC,
                    joinLogs
                            .sortedBy { it.timestamp }
                            .firstOrNull { this.id in it.mentionedUserIds }
                            ?.id?.timeStampUTC ?: this.id.timeStampUTC
            ),
            messageSequence
                    .filter { it.guildId eq this.guild.id.longValue }
                    .filter { it.authorId eq this.id.longValue }
                    .minBy { it.epochSecond }
                    ?.toDateTime() ?: this.id.timeStampUTC
    )
}

suspend fun Member.ifHasPermission(messageChannel: MessageChannel,
                           vararg permissions: Permission,
                           thingToRunIfPermissionPresent: () -> Unit) {
    var shouldRun = true

    val memberPermissionsLacked = permissions.filterNot { it in this.getPermissions() }

    if (memberPermissionsLacked.isNotEmpty()) {
        messageChannel.createMessage("${this.displayName}, " +
                "you do not have ${memberPermissionsLacked.joinToString(separator = " or ") { it.toString() }} permissions.")

        shouldRun = false
    }

    val selfMember = this.kord.getSelf().asMemberOrNull(guildId)
    val selfPermissionsLacked = permissions.filterNot { it in this.getPermissions() }


    if (selfPermissionsLacked.isNotEmpty()) {
        messageChannel.createMessage(selfMember?.displayName +
                " does not have ${selfPermissionsLacked.joinToString(separator = " or ") { it.toString() }} permissions.")

        shouldRun = false
    }
    if (shouldRun) {
        thingToRunIfPermissionPresent()
    }
}