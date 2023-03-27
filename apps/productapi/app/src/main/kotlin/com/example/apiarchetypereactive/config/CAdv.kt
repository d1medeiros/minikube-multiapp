package com.example.apiarchetypereactive.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponseException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class CAdv : ResponseEntityExceptionHandler() {
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    override fun handleErrorResponseException(
        ex: ErrorResponseException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        log.error("error", ex)
        return super.handleErrorResponseException(ex, headers, status, request)
    }
}