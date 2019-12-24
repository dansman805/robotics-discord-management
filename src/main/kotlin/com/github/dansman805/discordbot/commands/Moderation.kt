package com.github.dansman805.discordbot.commands

import me.aberrantfox.kjdautils.api.dsl.Precondition
import me.aberrantfox.kjdautils.api.dsl.command.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.api.dsl.precondition
import me.aberrantfox.kjdautils.internal.arguments.MemberArg
import me.aberrantfox.kjdautils.internal.arguments.SentenceArg
import me.aberrantfox.kjdautils.internal.arguments.UserArg
import me.aberrantfox.kjdautils.internal.command.Fail
import me.aberrantfox.kjdautils.internal.command.Pass
import net.dv8tion.jda.api.Permission

const val modCommandCategoryName = "Moderation"

@Precondition
fun isMod() = precondition {
    if (it.container[it.commandStruct.commandName]?.category != modCommandCategoryName ) {
        return@precondition Pass
    }

    if (it.message.member?.hasPermission(Permission.BAN_MEMBERS) == true) {
        return@precondition Pass
    } else {
        return@precondition Fail("You must be a mod to run this command!")
    }
}


//TODO: fix delDays; maybe make it an arg or something?
@CommandSet(modCommandCategoryName)
fun modCommands() = commands {
    command("Ban") {
        description = "Bans someone in the guild for a given reason"

        execute(MemberArg, SentenceArg.makeOptional("No reason provided.")) {
            it.guild?.ban(it.args.first, 0)
        }
    }

    command("Unban") {
        description = "Unbans someone in the guild"

        execute(UserArg, SentenceArg.makeOptional("No reason provided.")) {
            it.guild?.unban(it.args.first)
        }
    }

    command("Kick") {
        description = "Kick someone in the guild for a given reason"

        execute(MemberArg, SentenceArg.makeOptional("No reason provided.")) {
            it.guild?.kick(it.args.first, it.args.second)
        }
    }
}