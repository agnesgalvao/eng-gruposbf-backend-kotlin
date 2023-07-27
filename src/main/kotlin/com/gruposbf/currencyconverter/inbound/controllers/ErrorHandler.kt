package com.gruposbf.currencyconverter.inbound.controllers

import com.gruposbf.currencyconverter.application.CurrenciesApplication
import com.gruposbf.currencyconverter.infra.gateways.exceptions.ClientException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.rmi.UnexpectedException

@RestControllerAdvice
class ErrorHandler {
    private val logger = LoggerFactory.getLogger(CurrenciesApplication::class.java)
    @ExceptionHandler(ClientException::class)
    fun handleClientException(ex: (ClientException)): ResponseEntity<ErrorResponse> {
        logger.error(ex.message)

        val errorResponse = ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Client error: ${ex.message}")
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(UnexpectedException::class)
    fun handleUnexpectedException(ex: (UnexpectedException)): ResponseEntity<ErrorResponse> {
        logger.error(ex.message)

        val errorResponse = ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Client error: ${ex.message}")
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleBadRequestException(ex: (MethodArgumentTypeMismatchException)): ResponseEntity<ErrorResponse> {
        logger.error(ex.message)

        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST, "Bad request error: ${ex.message}")
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    data class ErrorResponse(
        val status: HttpStatus,
        val message: String
    )
}
