package com.github.dansman805.discordbot.commands

import com.github.dansman805.discordbot.services.StatisticsService
import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.annotation.Precondition
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.internal.arguments.*
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

        description = "Graphs the cumulative amount of messages for either a provided user or the guild."

        execute(UserArg(allowsBot = true).makeNullableOptional(),
                WordArg.makeOptional { "" },
                IntegerArg.makeOptional { 7 },
                BooleanArg.makeOptional { false }) {
            statistics.cumulativeMessages(it.args.first, it.guild!!,
                    it.message.textChannel,
                    it.args.second.replace("`", ""),
                    it.args.third,
                    it.args.fourth
            )
        }
    }

    command("Messages") {
        requiresGuild = true

        description = "Graphs the amount of messages for either a provided user or the guild."

        execute(UserArg(allowsBot = true).makeNullableOptional(),
                WordArg.makeOptional { "" },
                IntegerArg.makeOptional { 7 },
                BooleanArg.makeOptional { false }) {
            statistics.messages(it.args.first, it.guild!!,
                    it.message.textChannel,
                    it.args.second.replace("`", ""),
                    it.args.third,
                    it.args.fourth
            )
        }
    }

    command("MessageRanking") {
        requiresGuild = true

        description = "Graphs the ranking of members by messages of the server with an optional filter."

        execute(WordArg.makeOptional { "" },
                IntegerArg.makeOptional { 20 },
                BooleanArg.makeOptional { false }) {
            statistics.messageRanking(
                    it.guild!!,
                    it.message.textChannel,
                    it.args.first.replace("`", ""),
                    it.args.second,
                    it.args.third
            )
        }
    }

    command("ChannelDistribution") {
        requiresGuild = true

        description = "Graphs the amount of messages in each channel for either a provided user or the guild."

        execute(UserArg(allowsBot = true).makeNullableOptional(), BooleanArg.makeOptional { false }) {
            statistics.channelDistribution(it.args.first, it.guild!!, it.message.textChannel, it.args.second)
        }
    }
}
