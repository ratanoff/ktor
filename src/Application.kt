package com.example

import com.example.main.kotlin.repo.TopRepository
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.request.path
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import main.kotlin.repo.SearchRepository
import org.slf4j.event.Level

data class ToDo(var id: Int, val name: String, val description: String)

fun main(args: Array<String>) {
    val toDoList = ArrayList<ToDo>()

    embeddedServer(Netty, port = 8080) {

        install(CallLogging) {
            level = Level.DEBUG
            filter { call -> call.request.path().startsWith("/api") }
        }

        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }

        routing {
            route("/api") {
                get("/top") {
                    call.respond(TopRepository.getTop("0")!!)
                }
                get("/search/{query}") {
                    call.respond(SearchRepository.search(call.parameters["query"]!!)!!)
                    call.respond("Query = ${call.parameters["query"]}")
                }
            }


            route("/todo") {
                post {
                    val toDo = call.receive<ToDo>()
                    toDo.id = toDoList.size
                    toDoList.add(toDo)
                    call.respond("Added")
                }
                delete("/{id}") {
                    call.respond(toDoList.removeAt(call.parameters["id"]!!.toInt()))
                    call.respond("Deleted")
                }
                get("/{id}") {
                    call.respond(toDoList[call.parameters["id"]!!.toInt()])
                }
                get {
                    call.respond(toDoList)
                }
            }
        }
    }.start(wait = true)
}





