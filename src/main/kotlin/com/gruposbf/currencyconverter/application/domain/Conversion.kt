package com.gruposbf.currencyconverter.application.domain

import com.gruposbf.currencyconverter.infra.repositories.resources.CurrencyEntity
import java.math.BigDecimal

data class Conversion(
    val currency: Currency,
    val convertedAmount: BigDecimal
)

data class Currency(
    val name: CurrencyEntity.CurrencyName,
    val quotation: BigDecimal
)
