package com.github.dansman805.discordbot.arguments

import com.github.dansman805.discordbot.dataclasses.Motor
import me.aberrantfox.kjdautils.api.dsl.command.CommandEvent
import me.aberrantfox.kjdautils.internal.command.ArgumentResult
import me.aberrantfox.kjdautils.internal.command.ArgumentType
import me.aberrantfox.kjdautils.internal.command.ConsumptionType

object MotorArg : ArgumentType<Motor>() {
    override val name = "Motor"
    override val consumptionType = ConsumptionType.Single
    override val examples = arrayListOf("[Stock393, HighSpeed393, NeveRest, YellowJacket, or RevHDHex]")

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Motor> {
        return when (arg.toLowerCase()) {
            "stock393".toLowerCase() -> ArgumentResult.Success(Motor.stock393)
            "highspeed393".toLowerCase() -> ArgumentResult.Success(Motor.highSpeed393)
            "neverest".toLowerCase() -> ArgumentResult.Success(Motor.neverRestBare)
            "yellowjacket".toLowerCase() -> ArgumentResult.Success(Motor.neverRestBare)
            "revhdhex".toLowerCase() -> ArgumentResult.Success(Motor.neverRestBare)
            else -> ArgumentResult.Error("Expected a motor, got $arg")
        }
    }
}
