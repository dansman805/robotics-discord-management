package com.github.dansman805.discordbot.arguments

import com.github.dansman805.discordbot.dataclasses.Motor
import me.aberrantfox.kjdautils.api.dsl.command.CommandEvent
import me.aberrantfox.kjdautils.internal.command.ArgumentResult
import me.aberrantfox.kjdautils.internal.command.ArgumentType
import me.aberrantfox.kjdautils.internal.command.ConsumptionType

object MotorArg : ArgumentType<Motor>() {
    override val name = "Motor"
    override val consumptionType = ConsumptionType.Single
    override val examples = arrayListOf("[Stock393, HighSpeed393, NeveRest60, NeveRest40, NeveRest20, NeveRestBare, " +
            "or HDHex40]")

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Motor> {
        return when (arg) {
            "Stock393" -> ArgumentResult.Success(Motor.stock393)
            "HighSpeed393" -> ArgumentResult.Success(Motor.highSpeed393)
            "NeveRest60" -> ArgumentResult.Success(Motor.neveRest60)
            "NeveRest40" -> ArgumentResult.Success(Motor.neveRest40)
            "NeveRest20" -> ArgumentResult.Success(Motor.neveRest20)
            "NeveRestBare" -> ArgumentResult.Success(Motor.neverRestBare)
            "HDHex40" -> ArgumentResult.Success(Motor.hdHex40)
            else -> ArgumentResult.Error("Expected a motor, got $arg")
        }
    }
}
