package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.extensions.safe
import com.github.dansman805.discordbot.extensions.toEmbed
import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.aberrantfox.kjdautils.internal.arguments.MemberArg

@CommandSet("Info")
fun infoCommands() = commands {
    command("Member", "UserInformation", "UserInfo", "User") {
        description = "Retrieve information about a member of the guild."
        requiresGuild = true

        execute(MemberArg.makeNullableOptional()) {
            it.safe {
                if (it.args.first == null) {
                    it.respond(it.author.toMember(it.guild!!)!!.toEmbed())
                }
                else {
                    it.respond(it.args.first!!.toEmbed())
                }
            }
        }
    }

    command("Guild", "Server", "GuildInformation", "ServerInformation", "GuildInfo", "ServerInfo") {
        description = "Retrieve information about this guild."

        execute {
            it.safe {
                it.respond(it.guild!!.toEmbed())
            }
        }
    }
}
