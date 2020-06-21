package com.github.dansman805.discordbot.arguments

import com.github.dansman805.discordbot.dataclasses.Motor
import me.jakejmattson.kutils.api.dsl.arguments.ArgumentResult
import me.jakejmattson.kutils.api.dsl.arguments.ArgumentType
import me.jakejmattson.kutils.api.dsl.command.CommandEvent

object MotorArg : ArgumentType<Motor>() {
    override val name = "Motor"

    override fun generateExamples(event: CommandEvent<*>) =
            arrayListOf("[Stock393, HighSpeed393, NeveRest, YellowJacket, or RevHDHex]")

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Motor> {
        return when (arg.toLowerCase()) {
            "stock393".toLowerCase() -> ArgumentResult.Success(Motor.stock393)
            "highspeed393".toLowerCase() -> ArgumentResult.Success(Motor.highSpeed393)
            "neverest".toLowerCase() -> ArgumentResult.Success(Motor.neverRestBare)
            "yellowjacket".toLowerCase() -> ArgumentResult.Success(Motor.neverRestBare)
            "revhdhex".toLowerCase() -> ArgumentResult.Success(Motor.neverRestBare)
            "nr".toLowerCase() -> ArgumentResult.Success(Motor.neverRestBare)
            else -> ArgumentResult.Error("Expected a motor, got $arg")
        }
    }
}
