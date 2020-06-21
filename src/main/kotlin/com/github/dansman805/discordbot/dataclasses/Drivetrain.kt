package com.github.dansman805.discordbot.dataclasses

import com.github.dansman805.discordbot.extensions.pretty
import me.jakejmattson.kutils.api.dsl.embed.embed
import kotlin.math.PI

data class Drivetrain(val motor: Motor, val wheelDiameter: Double) {
    private val wheelCircumference = PI * wheelDiameter

    private val freeSpeed = motor.rpm * wheelCircumference / 60.0

    fun toEmbed() = embed {
        title {
            text = "Drivetrain Statistics (Unit is that of the wheel diameter)"
        }

        field {
            this.name = "Free Speed"
            this.value = "${freeSpeed.pretty} units/second"
        }

        field {
            this.name = "Wheel Circumference"
            this.value = "${wheelCircumference.pretty} units"
        }
    }

}