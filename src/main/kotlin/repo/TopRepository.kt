package com.example.main.kotlin.repo

import com.example.main.kotlin.entity.Cookies
import com.example.main.kotlin.entity.TopFilm
import org.jsoup.Jsoup
import java.io.IOException
import java.net.URL

object TopRepository {

    fun getTop(category: String): List<TopFilm>? {
        val items = ArrayList<TopFilm>()
        val BASE_URL = "https://kinozal.guru/top.php"

        val url = URL("$BASE_URL?t=$category").toString()


        try {
            val doc = Jsoup
                .connect(url)
                .cookies(Cookies.getCookies())
                .get()

            val elements = doc.select("div.bx1").select("a")
            elements.forEach { element ->
                val link = BASE_URL + element.select("a").attr("href")
                val tmpPictureUrl = element.select("a").select("img").attr("src")
                val posterUrl = if (tmpPictureUrl.contains("poster")) "http://kinozal.guru$tmpPictureUrl" else tmpPictureUrl

                val topFilm = TopFilm(posterUrl, link)
                items.add(topFilm)
            }

            return items
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}