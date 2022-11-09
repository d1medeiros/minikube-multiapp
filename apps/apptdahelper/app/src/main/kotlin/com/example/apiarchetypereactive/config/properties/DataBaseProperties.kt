package com.example.apiarchetypereactive.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties



@ConfigurationProperties("db.r2dbc")
class DataBaseProperties(
    var username: String? = null,
    var password: String? = null,
    var url: String? = null,
    var port: Int? = null,
    var name: String? = null,
    var maximumMessageSize: Int? = 100_000,
)
