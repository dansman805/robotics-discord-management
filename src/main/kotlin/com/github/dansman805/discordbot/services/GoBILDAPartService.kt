package com.github.dansman805.discordbot.services

import com.github.kittinunf.fuel.httpGet
import me.aberrantfox.kjdautils.api.annotation.Service
import org.jsoup.Jsoup

@Service
class GoBILDAPartService {
    fun getProduct(searchTerm: String): String {
        val searchUrl = "https://www.gobilda.com/search/search-results/?search_query=$searchTerm"

        return searchUrl.httpGet().response().second.toString()
    }
}