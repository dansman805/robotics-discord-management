package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.arguments.GearRatioArg
import com.github.dansman805.discordbot.arguments.MotorArg
import com.github.dansman805.discordbot.dataclasses.Drivetrain
import com.github.dansman805.discordbot.dataclasses.Motor
import com.github.dansman805.discordbot.extensions.safe
import kotlinx.serialization.ImplicitReflectionSerializer
import me.jakejmattson.kutils.api.annotations.CommandSet
import me.jakejmattson.kutils.api.arguments.DoubleArg
import me.jakejmattson.kutils.api.dsl.command.commands



/**
 *
 * @param stallTorque in N-m
 * @param stallCurrent in Amps
 * @param freeCurrent in Amps
 * @param power in Watts
 */

@ImplicitReflectionSerializer
@CommandSet("Mechanism Analysis")
fun jvnCommands() = commands {
    command("Motor") {
        description = "Provides statistics about a motor"

        execute(MotorArg) {
            it.safe {
                it.respond(it.args.first.genEmbed())
            }
        }
    }

    command("Gear") {
        description = "Provides statistics about a motor when geared"

        execute(GearRatioArg, MotorArg.makeOptional(Motor.neverRestBare)) {
            it.safe {
                val motor = it.args.second.gear(it.args.first)

                it.respond(motor.genEmbed("Geared Motor Statistics"))
            }
        }
    }

    command("Drivetrain", "DT") {
        description = "Provides statistics about a drivetrain with a wheel diameter, gear ratio and optionally a motor"

        execute(GearRatioArg, DoubleArg, MotorArg.makeOptional(Motor.neverRestBare)) {
            it.safe {
                val motor = it.args.third.gear(it.args.first)
                val drivetrain = Drivetrain(motor, it.args.second)

                it.respond(drivetrain.toEmbed())
            }
        }
    }
}