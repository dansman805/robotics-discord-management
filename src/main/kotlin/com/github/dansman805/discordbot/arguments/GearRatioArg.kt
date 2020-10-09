package com.github.dansman805.discordbot.arguments

import me.jakejmattson.discordkt.api.arguments.ArgumentType
import me.jakejmattson.discordkt.api.arguments.ArgumentResult
import me.jakejmattson.discordkt.api.arguments.Success
import me.jakejmattson.discordkt.api.arguments.Error
import me.jakejmattson.discordkt.api.dsl.CommandEvent

object GearRatioArg : ArgumentType<Double>() {
    override val name = "Gear Ratio"
    override fun generateExamples(event: CommandEvent<*>) = arrayListOf("3:1,5:1,8:5", "1:2,4:1")

    override suspend fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Double> =
            try {
                val gearSizes = arg.split(":")
                        .map { it.split(",") }
                        .flatten()
                        .map { it.replace("\\s".toRegex(), "") }
                        .map { it.toDouble() }

                Success(gearSizes.reduce { gearRatio, gearSize -> gearSize / gearRatio})
            } catch (e: Exception) {
                Error("Expected a gear ratio, got $arg")
            }
}