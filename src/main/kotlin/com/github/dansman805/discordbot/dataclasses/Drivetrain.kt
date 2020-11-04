package com.github.dansman805.discordbot.dataclasses

import com.github.dansman805.discordbot.extensions.pretty
import com.gitlab.kordlib.core.behavior.channel.createEmbed
import com.gitlab.kordlib.core.entity.channel.MessageChannel
import kotlin.math.PI

data class Drivetrain(val motor: Motor, val wheelDiameter: Double) {
    private val wheelCircumference = PI * wheelDiameter

    private val freeSpeed = motor.rpm * wheelCircumference / 60.0

    suspend fun sendEmbed(channel: MessageChannel)  {
        channel.createEmbed {
            title = "Drivetrain Statistics (Unit is that of wheel diameter)"

            field {
                name = "Free Speed"
                value = "${freeSpeed.pretty} units/second"
            }

            field {
                name = "Wheel Circumference"
                value = "${wheelCircumference.pretty} units"
            }
        }
    }

}