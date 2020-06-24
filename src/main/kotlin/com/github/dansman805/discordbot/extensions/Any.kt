package com.github.dansman805.discordbot.extensions

import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberExtensionFunctions
import kotlin.reflect.full.memberProperties

val Any.properties: String
    get() {
        val sb = StringBuilder()
        this::class.memberProperties.forEach { member ->
            if (member.visibility == KVisibility.PUBLIC) {
                sb.append("${member.name}: ${member.getter.call(this)}\n")
            }
        }

        this::class.declaredFunctions.forEach { member ->
            if (member.visibility == KVisibility.PUBLIC) {
                sb.append("${member.name}\n")
            }
        }


        this::class.declaredMemberExtensionFunctions.forEach { member ->
            if (member.visibility == KVisibility.PUBLIC) {
                sb.append("${member.name}\n")
            }
        }

        return sb.toString()
    }