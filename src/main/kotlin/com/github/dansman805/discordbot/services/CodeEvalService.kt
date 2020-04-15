package com.github.dansman805.discordbot.services

import me.aberrantfox.kjdautils.api.annotation.Service
import de.swirtz.ktsrunner.objectloader.KtsObjectLoader
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Emote

var jda: JDA? = null

@Service
class CodeEvalService {
    private val objectLoader = KtsObjectLoader()

    fun runCode(code: String, jdaArg: JDA): String? {
        jda = jdaArg

        return try {
            objectLoader.load<Any>(
                    "import com.github.dansman805.discordbot.services.jda\n" +
                            "import net.dv8tion.jda.*\n" +
                            "import me.aberrantfox.kjdautils.*\n" +
                            code).toString()
        }
        catch (e: IllegalArgumentException) {
            null
        }
    }
}