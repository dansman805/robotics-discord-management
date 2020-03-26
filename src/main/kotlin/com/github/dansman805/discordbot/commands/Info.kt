package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.extensions.memberCount
import com.github.dansman805.discordbot.extensions.toEmbed
import com.github.dansman805.discordbot.extensions.toHexString
import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.internal.arguments.MemberArg

@CommandSet("Info")
fun infoCommands() = commands {
    command("Member", "UserInformation", "UserInfo", "User") {
        description = "Retrieve information about a member of the guild."

        execute(MemberArg) {
            it.respond(it.args.first.toEmbed())
        }

    }

    command("Guild", "Server", "GuildInformation", "ServerInformation", "GuildInfo", "ServerInfo") {
        description = "Retrieve information about this guild."

        execute {
            it.respond(it.guild!!.toEmbed())
        }
    }
}
