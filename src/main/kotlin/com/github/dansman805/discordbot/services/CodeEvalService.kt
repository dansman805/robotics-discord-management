package com.github.dansman805.discordbot.services

import me.aberrantfox.kjdautils.api.annotation.Service
import de.swirtz.ktsrunner.objectloader.KtsObjectLoader
import de.swirtz.ktsrunner.objectloader.LoadException
import net.dv8tion.jda.api.JDA
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Exception

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
        catch (e: Exception) {
            when (e) {
                is IllegalArgumentException -> null
                is LoadException -> {
                    val stringWriter = StringWriter()
                    e.printStackTrace(PrintWriter(stringWriter))
                    stringWriter.toString().substringAfter("Caused by:")
                }
                else -> throw e
            }
        }
    }
}