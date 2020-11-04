package com.github.dansman805.discordbot.extensions

import com.github.dansman805.discordbot.botConfig
import com.gitlab.kordlib.common.entity.Snowflake
import java.time.ZoneOffset

val Snowflake.timeStampUTC
    get() = this.timeStamp.atOffset(ZoneOffset.UTC)!!

val Snowflake.formattedTimeStamp
    get() = this.timeStampUTC.format(botConfig.dateTimeFormatter)!!