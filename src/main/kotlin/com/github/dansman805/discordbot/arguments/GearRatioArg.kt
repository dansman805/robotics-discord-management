package com.github.dansman805.discordbot.arguments

import me.aberrantfox.kjdautils.api.dsl.command.CommandEvent
import me.aberrantfox.kjdautils.internal.command.ArgumentResult
import me.aberrantfox.kjdautils.internal.command.ArgumentType
import me.aberrantfox.kjdautils.internal.command.ConsumptionType

object GearRatioArg : ArgumentType<Double>() {
    override val name = "Gear Ratio"
    override val examples = arrayListOf("3:1,5:1,8:5", "1:2,4:1")
    override val consumptionType = ConsumptionType.All

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Double> =
            try {
                val gearSizes = arg.split(":")
                        .map { it.split(",") }
                        .flatten()
                        .map { it.replace("\\s".toRegex(), "") }
                        .map { it.toDouble() }
                ArgumentResult.Success(gearSizes.reduce { gearRatio, gearSize -> gearSize / gearRatio})
            } catch (e: Exception) {
                ArgumentResult.Error("Expected a gear ratio, got $arg")
            }
}