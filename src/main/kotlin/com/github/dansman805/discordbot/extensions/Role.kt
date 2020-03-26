package com.github.dansman805.extensions

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role

fun Role.memberCount(): Int = members().count()
fun Role.members(): List<Member> = this.guild.members.filter { this in it.roles}