package com.github.dansman805.discordbot.schemas

import kotlinx.serialization.Serializable
import me.aberrantfox.kjdautils.api.dsl.embed
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import java.awt.Color

@Serializable
data class FTCTeam(val team_key: String?,
                   val region_key: String?,
                   val league_key: String?,
                   val team_number: Int?,
                   val team_name_short: String?,
                   val team_name_long: String?,
                   val robot_name: String?,
                   val last_active: Int?,
                   val city: String?,
                   val state_prov: String?,
                   val zip_code: String?,
                   val country: String?,
                   val rookie_year: Int?,
                   val website: String?) {
    fun location() = "$city, $state_prov $zip_code, $country"

    fun lastActive(): String = when (last_active) {
        null -> "?"
        else -> yearToSeason("20${last_active.toString().substring(2..3)}".toInt())
    }

    fun genEmbed(): MessageEmbed {
        val e = EmbedBuilder()

        e.setTitle("FIRST®️ Tech Challenge Team $team_number",
                "https://www.thebluealliance.com/team/$team_number")
        e.setColor(0xf89808)
        e.setThumbnail("https://raw.githubusercontent.com/orange-alliance/the-orange-alliance/master/src/assets/imgs/icon512.png")

        e.addField("Name", team_name_short, true)
        e.addField("Rookie Year", if (rookie_year != null) yearToSeason(rookie_year + 1) else "?", true)
        e.addField("Last Active", lastActive(), true)
        e.addField("Location", location(), true)
        e.addField("Website", website ?: "n/a", false)

        return e.build()
    }
    
    companion object {
        fun yearToSeason(year: Int): String = when(year) {
            2020 -> "2019-2020: Skystone"
            2019 -> "2018-2019: Rover Ruckus"
            2018 -> "2017-2018: Relic Recovery"
            2017 -> "2016-2017: Velocity Vortex"
            2016 -> "2015-2016: RES-Q"
            2015 -> "2014-2015: Cascade Effect"
            2014 -> "2013-2014: Block Party!"
            2013 -> "2012-2013: Ring It Up!"
            2012 -> "2011-2012: Bowled Over!"
            2011 -> "2010-2011: Get Over It!"
            2010 -> "2009-2010: Hot Shot!"
            2009 -> "2008-2009: Face Off"
            2008 -> "2007-2008: Quad Quandary"
            else -> year.toString()
        }
    }
}
