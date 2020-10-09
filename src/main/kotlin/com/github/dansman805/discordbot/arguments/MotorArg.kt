package com.github.dansman805.discordbot.arguments

import com.github.dansman805.discordbot.dataclasses.Motor
import me.jakejmattson.discordkt.api.arguments.ArgumentResult
import me.jakejmattson.discordkt.api.arguments.ArgumentType
import me.jakejmattson.discordkt.api.arguments.Success
import me.jakejmattson.discordkt.api.arguments.Error
import me.jakejmattson.discordkt.api.dsl.CommandEvent

object MotorArg : ArgumentType<Motor>() {
    override val name = "Motor"

    override fun generateExamples(event: CommandEvent<*>) =
            arrayListOf("[Stock393, HighSpeed393, NeveRest, YellowJacket, or RevHDHex]")

    override suspend fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Motor> {
        return when (arg.toLowerCase()) {
            "stock393".toLowerCase() -> Success(Motor.stock393)
            "highspeed393".toLowerCase() -> Success(Motor.highSpeed393)
            "neverest".toLowerCase() -> Success(Motor.neverRestBare)
            "yellowjacket".toLowerCase() -> Success(Motor.neverRestBare)
            "revhdhex".toLowerCase() -> Success(Motor.neverRestBare)
            "nr".toLowerCase() -> Success(Motor.neverRestBare)
            else -> Error("Expected a motor, got $arg")
        }
    }
}
