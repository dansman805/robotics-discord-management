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
                ArgumentResult.Success(arg.split(",")
                        .map { it.split(":") }
                        .map { it[1].toDouble() / it[0].toDouble() }
                        .reduce { n1, n2 -> n1 * n2 })
            } catch (e: Exception) {
                ArgumentResult.Error("Expected a gear ratio, got $arg")
            }
}