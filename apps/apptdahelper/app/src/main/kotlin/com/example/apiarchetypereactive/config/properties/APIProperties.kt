package com.example.apiarchetypereactive.config.properties

import org.springframework.boot.context.properties.bind.ConstructorBinding



class APIProperties @ConstructorBinding constructor(
    var hostname: String? = null,
    var authorization: AuthorizationProperties? = null
) {
    constructor() : this(null, null)


}

class AuthorizationProperties {
    val type: String? = null
    val token: String? = null
}

