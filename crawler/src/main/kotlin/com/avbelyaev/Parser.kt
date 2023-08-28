package com.avbelyaev

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.URI

class Parser(private val scopedToDomain: String) {

    fun extractLinks(document: Document, visited: Set<String>): List<String> {
        document.select("a[href*=#]").remove() // remove links starting with `#` e.g. https://monzo.com#mainContent
        return document.select("a").asSequence()
            .map { sanitizeUrl(it) }
            .filter { it.startsWith("http") }
            .filter { getDomainName(it) == scopedToDomain }
            .distinct()
            .filter { !visited.contains(it) }
            .toList()
    }

    private fun sanitizeUrl(htmlLink: Element): String = htmlLink.absUrl("href").removeSuffix("/")

    private fun getDomainName(url: String): String = URI(url).host.removePrefix("www.")
}
