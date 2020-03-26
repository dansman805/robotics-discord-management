package com.github.dansman805.discordbot.dataclasses

import kotlinx.serialization.Serializable
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed

@Serializable
data class FRCTeam(
        val key: String?,
        val team_number: Int?,
        val nickname: String?,
        val name: String?,
        val city: String?,
        val state_prov: String?,
        val country: String?,
        val address: String?,
        val postal_code: String?,
        val gmaps_place_id: String?,
        val gmaps_url: String?,
        val lat: Double?,
        val lng: Double?,
        val location_name: String?,
        val website: String?,
        val rookie_year: Int?,
        val motto: String?,
        val home_championship: Map<String?, String?>?) {
    fun location() = "$city, $state_prov $postal_code, $country"

    fun genEmbed(): MessageEmbed {
        val e = EmbedBuilder()

        e.setTitle("FIRST®️ Robotics Competition Team $team_number",
                "https://www.thebluealliance.com/team/$team_number")
        e.setColor(0x36469C)
        e.setThumbnail("https://frcavatars.herokuapp.com/get_image?team=$team_number")

        e.addField("Name", nickname, true)
        e.addField("Rookie Year", if (rookie_year != null) yearToSeason(rookie_year) else "?", true)
        e.addField("Location", location(), true)
        e.addField("Website", website ?: "n/a", false)

        return e.build()
    }

    companion object {
        fun yearToSeason(year: Int): String = when (year) {
            2020 -> "2020: Infinite Recharge"
            2019 -> "2019: Destination: Deep Space"
            2018 -> "2018: FIRST Power Up"
            2017 -> "2017: FIRST Steamworks"
            2016 -> "2016: FIRST Stronghold"
            2015 -> "2015: Recycle Rush"
            2014 -> "2014: Aerial Assist"
            2013 -> "2013: Ultimate Ascent"
            2012 -> "2012: Rebound Rumble"
            2011 -> "2011: Logo Motion"
            2010 -> "2010: Breakaway"
            2009 -> "2009: Lunacy"
            2008 -> "2008: FIRST Overdrive"
            2007 -> "2007: Rack 'n Roll"
            2006 -> "2006: Aim High"
            2005 -> "2005: Triple Play"
            2004 -> "2004: FIRST Frenzy: Raising the Bar"
            2003 -> "2003: Stack Attack"
            2002 -> "2002: Zone Zeal"
            2001 -> "2001: Diabolical Dynamics"
            2000 -> "2000: Co-Opertition FIRST"
            1999 -> "1999: Double Trouble"
            1998 -> "1998: Ladder Logic"
            1997 -> "1997: Toroid Terror"
            1996 -> "1996: Hexagon Havoc"
            1995 -> "1995: Ramp 'n Roll"
            1994 -> "1994: Tower Power"
            1993 -> "1993: Rug Rage"
            1992 -> "1992: Maize Craze"

            else -> year.toString()
        }
    }
}