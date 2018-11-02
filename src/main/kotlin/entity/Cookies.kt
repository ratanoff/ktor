package com.example.main.kotlin.entity

import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.IOException

object Cookies {

    private var cookies: Map<String, String>? = null

    fun getCookies(): Map<String, String>? {
        if (cookies == null) {
            cookies = updateCookies()
        }
        return cookies
    }

    private fun updateCookies(): Map<String, String> {

        var response: Connection.Response? = null

        try {
            response = Jsoup
                .connect("https://kinozal.guru/login.php")
                .method(Connection.Method.GET)
                .execute()

            response = Jsoup
                .connect("https://kinozal.guru/takelogin.php")
                .cookies(response!!.cookies())
                .data("username", "rbaloo")
                .data("password", "756530")
                .method(Connection.Method.POST)
                .followRedirects(true)
                .execute()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return response!!.cookies()
    }
}
