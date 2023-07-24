package com.gruposbf.currencyconverter.infra.repositories.resouces

import com.gruposbf.currencyconverter.infra.repositories.resources.CurrencyEntity
import java.math.BigDecimal
import java.time.LocalDate

class CurrencyEntityBuilder {

    var name = CurrencyEntity.CurrencyName.DOLLAR
    var quotation: BigDecimal = BigDecimal.TEN
    var createdAt: LocalDate = LocalDate.now()

    fun build() = CurrencyEntity(
        name = name,
        quotation = quotation,
        createdAt = createdAt,
    )
}
