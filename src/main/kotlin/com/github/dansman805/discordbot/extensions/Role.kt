package com.github.dansman805.discordbot.extensions

import com.gitlab.kordlib.core.any
import com.gitlab.kordlib.core.entity.Member
import com.gitlab.kordlib.core.entity.Role
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList

suspend fun Role.members(): List<Member> =
        this.guild.members.filter {
            member -> member.roles.any { it == this }
        }.toList()
