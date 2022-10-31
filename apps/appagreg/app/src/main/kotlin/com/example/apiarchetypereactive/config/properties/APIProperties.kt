package com.example.apiarchetypereactive.config.properties

import org.springframework.boot.context.properties.ConstructorBinding


@ConstructorBinding
class APIProperties {
    lateinit var hostname: String
    var authorization: AuthorizationProperties = AuthorizationProperties()
}

class AuthorizationProperties {
    val type: String? = null
    val token: String? = null
}

