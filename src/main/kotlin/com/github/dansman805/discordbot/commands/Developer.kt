package com.github.dansman805.discordbot.commands

import me.aberrantfox.kjdautils.api.dsl.Precondition
import me.aberrantfox.kjdautils.api.dsl.command.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.api.dsl.precondition
import me.aberrantfox.kjdautils.internal.command.*
import net.dv8tion.jda.api.Permission

const val adminCommandCategoryName = "Administrator"

@Precondition
fun isAdmin() = precondition {
    if (it.container[it.commandStruct.commandName]?.category != adminCommandCategoryName) {
        return@precondition Pass
    }

    if (it.message.member?.hasPermission(Permission.ADMINISTRATOR) == true) {
        return@precondition Pass
    } else {
        return@precondition Fail("You must be an admin to run this command!")
    }
}

@CommandSet(adminCommandCategoryName)
fun adminstratorCommands() = commands {

}