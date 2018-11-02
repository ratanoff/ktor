package main.kotlin.repo

import com.example.main.kotlin.entity.Cookies
import com.example.main.kotlin.entity.SearchItem
import org.jsoup.Jsoup
import java.io.IOException
import java.net.URL
import java.util.*

object SearchRepository {

    fun search(query: String, filter: String = "1", sort: String = "0"): List<SearchItem>? {

        val BASE_URL = "https://kinozal.guru"
        val SEARCH_URL = "$BASE_URL/browse.php"

        val items = ArrayList<SearchItem>()

        var pages = 0

        val url = URL("$SEARCH_URL?s=$query").toString()

        println(url)

        try {
            val doc = Jsoup
                    .connect(url)
                    .cookies(Cookies.getCookies())
                    .get()

            val elements = doc.select("td")
            for (element in elements) {
                if (element.text().contains("Найдено")) {
                    pages = getNumberPages(element.text())
                    break
                }
            }

            for (i in 0 until pages) {
                val pageUrl = URL("$SEARCH_URL?s=$query&t=$filter&f=$sort").toString()


                val document = Jsoup.connect(pageUrl)
                        .cookies(Cookies.getCookies())
                        .get()

                val sElements = document.select("tr.bg")

                for (element in sElements) {
                    val title = element.select("td.nam").select("a").text()
                    val link = BASE_URL + element.select("td.nam").select("a").attr("href")
                    val size = element.select("td.s")[1].text()
                    val seeds = element.select("td.sl_s").text()
                    val date = element.select("td.s")[2].text()

                    val item = SearchItem(title, link, size, seeds, date)

                    items.add(item)

                }
            }

            return items
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    private fun getNumberPages(found: String): Int {
        val count = found.split(" ")[1].toInt()
        val a = count / 50
        return if (a * 50 == count) a else a + 1
    }
}