package com.github.dansman805.dataclasses

import com.github.dansman805.discordbot.extensions.pretty
import me.aberrantfox.kjdautils.api.dsl.embed

data class Motor(val rpm: Double, val stallTorque: Double, val stallCurrent: Double, val freeCurrent: Double,
                 val power: Double) {
    fun gear(ratio: Double): Motor = this.copy(rpm = this.rpm * ratio, stallTorque = this.stallTorque / ratio)

    fun genEmbed(embedTitle: String = "Motor Statistics") = embed {
        title = embedTitle

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
        // data taken from
        // https://www.chiefdelphi.com/t/paper-ftc-bw-jvns-mechanical-design-calculator-revised-for-ftc/160196
        val stock393 = Motor(167.0, 2.78, 8.00, 0.62, 12.14)
        val highSpeed393 = Motor(267.0, 1.73, 8.00, 0.62, 12.10)
        val neveRest60 = Motor(105.0, 4.19, 11.50, 0.40, 14.00)
        val neveRest40 = Motor(129.0, 2.80, 11.50, 0.40, 14.00)
        val neveRest20 = Motor(315.0, 1.39, 11.50, 0.40, 14.00)
        val neverRestBare = Motor(6600.0, 0.06178857854, 11.5, 0.4, 14.00)
        val hdHex40 = Motor(150.0, 2.796374526, 11.5, 0.4, 15.00)
    }
}