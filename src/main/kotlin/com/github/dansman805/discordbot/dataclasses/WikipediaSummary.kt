package com.github.dansman805.discordbot.dataclasses

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.serialization.responseObject
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed

@kotlinx.serialization.ImplicitReflectionSerializer
@kotlinx.serialization.UnstableDefault
@Serializable
data class WikipediaSummary(
        val type: String, val title: String, val extract: String, val pageid: Int, val thumbnail: Image? = null) {
    fun toEmbed(): MessageEmbed {
        val e = EmbedBuilder()
        e.setTitle("Wikipedia: $title", "http://en.wikipedia.org/?curid=$pageid")

        e.setImage(thumbnail?.source)

        if (type == "disambiguation") {
            /*for (link in getLinksOfDisambiguation(title)) {
                e.addField(link, "", true)
            }*/

            e.addField("Disambiguation", "" +
                    "There is a page for this topic. However, it is a disambiguation; please try being more specific.",
                    false)
        }
        else {
            val shortenedExtract = if (extract.length > 1023) extract.substring(0..1023) else extract

            e.addField("Summary", shortenedExtract, false)
        }

        return e.build()
    }
}

@Serializable
data class Image(val source: String)

@kotlinx.serialization.ImplicitReflectionSerializer
@kotlinx.serialization.UnstableDefault
fun getLinksOfDisambiguation(title: String): List<String> {
    val disambiguation =
            Fuel.get("https://en.wikipedia.org/w/api.php?action=parse&page=$title&format=json")
                    .responseObject<Disambiguation>(json = Json.nonstrict).third

    return disambiguation.get().parse.links.links.filterNotNull().map { it.`*` }
}

@Serializable
data class Disambiguation(val parse: Parse)

@Serializable
data class Parse(val links: Links)

@Serializable
data class Links(val `0`: Link?=null,
                 val `1`: Link?=null,
                 val `2`: Link?=null,
                 val `3`: Link?=null,
                 val `4`: Link?=null,
                 val `5`: Link?=null,
                 val `6`: Link?=null,
                 val `7`: Link?=null,
                 val `8`: Link?=null,
                 val `9`: Link?=null,
                 val `10`: Link?=null,
                 val `11`: Link?=null,
                 val `12`: Link?=null,
                 val `13`: Link?=null,
                 val `14`: Link?=null,
                 val `15`: Link?=null,
                 val `16`: Link?=null,
                 val `17`: Link?=null,
                 val `18`: Link?=null,
                 val `19`: Link?=null,
                 val `20`: Link?=null,
                 val `21`: Link?=null,
                 val `22`: Link?=null,
                 val `23`: Link?=null,
                 val `24`: Link?=null,
                 val `25`: Link?=null,
                 val `26`: Link?=null,
                 val `27`: Link?=null,
                 val `28`: Link?=null,
                 val `29`: Link?=null,
                 val `30`: Link?=null,
                 val `31`: Link?=null,
                 val `32`: Link?=null,
                 val `33`: Link?=null,
                 val `34`: Link?=null,
                 val `35`: Link?=null,
                 val `36`: Link?=null,
                 val `37`: Link?=null,
                 val `38`: Link?=null,
                 val `39`: Link?=null,
                 val `40`: Link?=null,
                 val `41`: Link?=null,
                 val `42`: Link?=null,
                 val `43`: Link?=null,
                 val `44`: Link?=null,
                 val `45`: Link?=null,
                 val `46`: Link?=null,
                 val `47`: Link?=null,
                 val `48`: Link?=null,
                 val `49`: Link?=null,
                 val `50`: Link?=null,
                 val `51`: Link?=null,
                 val `52`: Link?=null,
                 val `53`: Link?=null,
                 val `54`: Link?=null,
                 val `55`: Link?=null,
                 val `56`: Link?=null,
                 val `57`: Link?=null,
                 val `58`: Link?=null,
                 val `59`: Link?=null,
                 val `60`: Link?=null,
                 val `61`: Link?=null,
                 val `62`: Link?=null,
                 val `63`: Link?=null,
                 val `64`: Link?=null,
                 val `65`: Link?=null,
                 val `66`: Link?=null,
                 val `67`: Link?=null,
                 val `68`: Link?=null,
                 val `69`: Link?=null,
                 val `70`: Link?=null,
                 val `71`: Link?=null,
                 val `72`: Link?=null,
                 val `73`: Link?=null,
                 val `74`: Link?=null,
                 val `75`: Link?=null,
                 val `76`: Link?=null,
                 val `77`: Link?=null,
                 val `78`: Link?=null,
                 val `79`: Link?=null,
                 val `80`: Link?=null,
                 val `81`: Link?=null,
                 val `82`: Link?=null,
                 val `83`: Link?=null,
                 val `84`: Link?=null,
                 val `85`: Link?=null,
                 val `86`: Link?=null,
                 val `87`: Link?=null,
                 val `88`: Link?=null,
                 val `89`: Link?=null,
                 val `90`: Link?=null,
                 val `91`: Link?=null,
                 val `92`: Link?=null,
                 val `93`: Link?=null,
                 val `94`: Link?=null,
                 val `95`: Link?=null,
                 val `96`: Link?=null,
                 val `97`: Link?=null,
                 val `98`: Link?=null,
                 val `99`: Link?=null) {
    val links = listOf(`0`,
            `1`, `2`, `3`, `4`, `5`, `6`, `7`, `8`, `9`, `10`, `11`, `12`, `13`, `14`, `15`, `16`, `17`, `18`, `19`,
            `20`, `21`, `22`, `23`, `24`, `25`, `26`, `27`, `28`, `29`, `30`, `31`, `32`,
            `33`,
            `34`,
            `35`,
            `36`,
            `37`,
            `38`,
            `39`,
            `40`,
            `41`,
            `42`,
            `43`,
            `44`,
            `45`,
            `46`,
            `47`,
            `48`,
            `49`,
            `50`,
            `51`,
            `52`,
            `53`,
            `54`,
            `55`,
            `56`,
            `57`,
            `58`,
            `59`,
            `60`,
            `61`,
            `62`,
            `63`,
            `64`,
            `65`,
            `66`,
            `67`,
            `68`,
            `69`,
            `70`,
            `71`,
            `72`,
            `73`,
            `74`,
            `75`,
            `76`,
            `77`,
            `78`,
            `79`,
            `80`,
            `81`,
            `82`,
            `83`,
            `84`,
            `85`,
            `86`,
            `87`,
            `88`,
            `89`,
            `90`,
            `91`,
            `92`,
            `93`,
            `94`,
            `95`,
            `96`,
            `97`,
            `98`,
            `99`)
}

@Serializable
data class Link(val `*`: String)