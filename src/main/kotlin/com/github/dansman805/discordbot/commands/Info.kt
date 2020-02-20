package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.dateTimeFormatter
import com.github.dansman805.discordbot.toEmbed
import com.github.dansman805.discordbot.toRGB
import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.extensions.jda.fullName
import me.aberrantfox.kjdautils.internal.arguments.MemberArg
import me.aberrantfox.kjdautils.internal.arguments.or
import java.awt.Color

@CommandSet("Info")
fun infoCommands() = commands {
    command("Member", "UserInformation", "UserInfo", "User") {
        description = "Retrieve information about a member of the guild."

        execute(MemberArg) {
            it.respond(it.args.first.toEmbed())
        }

    }

    command("Guild", "Server", "GuildInformation", "ServerInformation", "GuildInfo", "ServoInfo") {
        description = "Retrieve information about this guild."

        execute {
            it.respond(it.guild!!.toEmbed())
        }
    }
}
