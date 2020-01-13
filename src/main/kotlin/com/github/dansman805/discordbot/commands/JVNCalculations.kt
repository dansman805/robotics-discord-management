package com.github.dansman805.discordbot.commands

import kotlinx.serialization.ImplicitReflectionSerializer
import me.aberrantfox.kjdautils.api.dsl.command.CommandEvent
import me.aberrantfox.kjdautils.api.dsl.command.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.internal.command.ArgumentResult
import me.aberrantfox.kjdautils.internal.command.ArgumentType
import me.aberrantfox.kjdautils.internal.command.ConsumptionType

val Double.pretty: String
    get() = "${this}0000".substring(0, this.toString().indexOf(".") + 4)

/**
 *
 * @param stallTorque in N-m
 * @param stallCurrent in Amps
 * @param freeCurrent in Amps
 * @param power in Watts
 */
data class Motor(val rpm: Double, val stallTorque: Double, val stallCurrent: Double, val freeCurrent: Double,
                 val power: Double) {
    fun gear(ratio: Double): Motor = this.copy(rpm = this.rpm*ratio, stallTorque = this.stallTorque/ratio)

    fun genEmbed() = embed {
        title = "Geared Motor"

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
}

// data taken from https://www.chiefdelphi.com/t/paper-ftc-bw-jvns-mechanical-design-calculator-revised-for-ftc/160196
val stock393 = Motor(167.0,2.78, 8.00, 0.62, 12.14)
val highSpeed393 = Motor(267.0, 1.73, 8.00, 0.62, 12.10)
val neveRest60 = Motor(	105.0, 4.19, 11.50, 0.40, 14.00)
val neveRest40 = Motor(	129.0, 2.80, 11.50, 0.40, 14.00)
val neveRest20 = Motor(	315.0, 1.39, 11.50, 0.40, 14.00)
val neverRestBare = Motor(6600.0, 0.06178857854, 11.5, 0.4, 14.00)
val hdHex40 = Motor(150.0, 2.796374526,	11.5, 0.4, 15.00)

open class MotorArg : ArgumentType<Motor>() {
    companion object : MotorArg()

    override val name = "Motor"
    override val consumptionType = ConsumptionType.Single
    override val examples = arrayListOf("Stock393", "HighSpeed393", "NeveRest60", "NeveRest40", "NeveRest20",
            "NeveRestBare", "HDHex40")

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Motor> {
        return when (arg) {
            "Stock393" -> ArgumentResult.Success(stock393)
            "HighSpeed393" -> ArgumentResult.Success(highSpeed393)
            "NeveRest60" -> ArgumentResult.Success(neveRest60)
            "NeveRest40" -> ArgumentResult.Success(neveRest40)
            "NeveRest20" -> ArgumentResult.Success(neveRest20)
            "NeveRestBare" -> ArgumentResult.Success(neverRestBare)
            "HDHex40" -> ArgumentResult.Success(hdHex40)
            else -> ArgumentResult.Error("Expected a motor, got $arg")
        }
    }
}

open class GearRatioArg : ArgumentType<Double>() {
    companion object : GearRatioArg()
    override val name = "Gear Ratio"
    override val examples = arrayListOf("3:1,5:1,8:5", "1:2,4:1")
    override val consumptionType = ConsumptionType.All

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Double> =
            try {
                ArgumentResult.Success(arg.split(",")
                        .map { it.split(":") }
                        .map { it[1].toDouble() / it[0].toDouble() }
                        .reduce { n1, n2 -> n1 * n2 })
            }
            catch (e: Exception) {
                ArgumentResult.Error("Expected a gear ratio, got $arg")
            }
}

@ImplicitReflectionSerializer
@CommandSet("JVN Commands")
fun jvnCommands() = commands {
    command("Gear") {
        description = "Provides statistics about a drivetrain"

        execute(MotorArg, GearRatioArg) {
            val motor = it.args.first.gear(it.args.second)

            it.respond(motor.genEmbed())
        }
    }
}