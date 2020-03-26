package com.github.dansman805.commands

import com.github.dansman805.discordbot.arguments.GearRatioArg
import com.github.dansman805.discordbot.arguments.MotorArg
import kotlinx.serialization.ImplicitReflectionSerializer
import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands


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
            it.respond(it.args.first.genEmbed())
        }
    }

    command("Gear") {
        description = "Provides statistics about a motor when geared"

        execute(MotorArg, GearRatioArg) {
            val motor = it.args.first.gear(it.args.second)

            it.respond(motor.genEmbed("Geared Motor Statistics"))
        }
    }
}