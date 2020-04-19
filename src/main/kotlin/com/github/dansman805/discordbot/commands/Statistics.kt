package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.services.StatisticsService
import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.annotation.Precondition
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.internal.arguments.BooleanArg
import me.aberrantfox.kjdautils.internal.arguments.UserArg
import me.aberrantfox.kjdautils.internal.command.Fail
import me.aberrantfox.kjdautils.internal.command.Pass
import me.aberrantfox.kjdautils.internal.command.precondition
import net.dv8tion.jda.api.Permission

const val statisticsCommandCategoryName = "Statistics"

@Precondition
fun canUseStatistics() = precondition {
    if (it.container[it.commandStruct.commandName]?.category != statisticsCommandCategoryName) {
        return@precondition Pass
    }

    if (it.message.member?.hasPermission(Permission.BAN_MEMBERS) == true) {
        return@precondition Pass
    } else {
        return@precondition Fail("You must be a mod (or above) to run this command!")
    }
}

@CommandSet(statisticsCommandCategoryName)
fun statistics(statistics: StatisticsService) = commands {
    command("CumulativeMessages") {
        requiresGuild = true

        execute(UserArg.makeNullableOptional(), BooleanArg.makeOptional(false)) {
            statistics.cumulativeMessages(it.args.first, it.guild!!, it.message.textChannel, it.args.second)
        }
    }
}
