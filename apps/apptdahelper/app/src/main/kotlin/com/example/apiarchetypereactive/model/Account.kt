package com.example.apiarchetypereactive.model

class Account(
    var id: Long,
    var events: List<Event>
){

    fun add(events: List<Event>) {
        this.events = events
    }
}