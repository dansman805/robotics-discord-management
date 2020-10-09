package com.github.dansman805.discordbot.dataclasses

import com.github.dansman805.discordbot.extensions.pretty
import me.jakejmattson.kutils.api.dsl.embed.embed

data class Motor(val rpm: Double, val stallTorque: Double, val stallCurrent: Double, val freeCurrent: Double,
                 val power: Double) {
    fun gear(ratio: Double): Motor = this.copy(rpm = this.rpm * ratio, stallTorque = this.stallTorque / ratio)

    fun genEmbed(embedTitle: String = "Motor Statistics") = embed {
        title {
            text = embedTitle
        }

        field {
            name = "RPM"
            value = rpm.pretty
        }

        field {
            name = "Stall Torque"
            value = "${stallTorque.pretty} N-m"
            inline = true
        }

        field {
            name = "Stall Current"
            value = "${stallCurrent.pretty} amps"
            inline = true
        }

        field {
            name = "Free Current"
            value = "${freeCurrent.pretty} amps"
            inline = true
        }

        field {
            name = "Power"
            value = "${power.pretty} W"
            inline = true
        }
    }

    companion object {
        val stock393 = Motor(167.0, 2.78, 8.00, 0.62, 12.14)
        val highSpeed393 = Motor(267.0, 1.73, 8.00, 0.62, 12.10)
        val neverRestBare = Motor(5475.0, 0.173, 9.8, 0.355, 25.92)
    }
}