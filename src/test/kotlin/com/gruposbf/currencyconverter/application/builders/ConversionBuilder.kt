package com.gruposbf.currencyconverter.application.builders

import com.gruposbf.currencyconverter.application.domain.Conversion
import com.gruposbf.currencyconverter.application.domain.Currency
import com.gruposbf.currencyconverter.infra.repositories.resources.CurrencyEntity.CurrencyName.DOLLAR
import java.math.BigDecimal

private const val SCALE_LIMIT = 2

class ConversionBuilder {
    var currency = CurrencyBuilder().build()
    var convertedAmount: BigDecimal = BigDecimal.TEN.setScale(SCALE_LIMIT)
    fun build() = Conversion(
        currency = currency,
        convertedAmount
    )
}

class CurrencyBuilder {
    var name = DOLLAR
    var quotation: BigDecimal = BigDecimal.TEN

    fun build() = Currency(
        name = name,
        quotation = quotation
    )
}
