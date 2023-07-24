package com.gruposbf.currencyconverter.infra.repositories.resources

import com.gruposbf.currencyconverter.application.domain.Currency
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDate

@Document
data class CurrencyEntity(
    val name: CurrencyName,
    val quotation: BigDecimal,
    val createdAt: LocalDate = LocalDate.now(),
) {
    enum class CurrencyName(val code: String) {
        DOLLAR("USD"),
        EURO("EUR"),
        INDIAN_RUPEE("INR"),
    }

    fun toCurrency() = Currency(
        name = this.name,
        quotation = this.quotation
    )
}
