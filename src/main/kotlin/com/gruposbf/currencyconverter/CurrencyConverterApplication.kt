package com.gruposbf.currencyconverter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class CurrencyConverterApplication
fun main(args: Array<String>) {
    runApplication<CurrencyConverterApplication>(*args)
}
