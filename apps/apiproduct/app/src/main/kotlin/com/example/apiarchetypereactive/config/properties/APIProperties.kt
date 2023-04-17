package com.example.apiarchetypereactive.config.properties



class APIProperties {
    lateinit var url: String
    var authorization: AuthorizationProperties = AuthorizationProperties()
}

class AuthorizationProperties {
    val type: String? = null
    val token: String? = null
}

