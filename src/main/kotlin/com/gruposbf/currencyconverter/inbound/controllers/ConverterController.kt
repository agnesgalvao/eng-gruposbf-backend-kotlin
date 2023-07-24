package com.gruposbf.currencyconverter.inbound.controllers

import com.gruposbf.currencyconverter.application.ConverterApplication
import com.gruposbf.currencyconverter.inbound.controllers.resources.ConvertResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/api/convert")
class ConverterController(
    private val convertApplication: ConverterApplication
) {
    private val logger = LoggerFactory.getLogger(ConverterController::class.java)
    @GetMapping("/BRL/{amount}")
    fun convert(@PathVariable("amount") amount: BigDecimal): ResponseEntity<ConvertResponse> {
        logger.info("Receive GET request to /api/convert/BRL/$amount")
        return ResponseEntity(ConvertResponse(
            convertApplication
                .convert(amount)), HttpStatus.OK).also {
            logger.info("GET request to /api/convert/BRL/$amount finish with status code 200")
        }
    }
}
