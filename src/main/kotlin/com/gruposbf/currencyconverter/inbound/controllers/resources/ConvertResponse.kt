package com.gruposbf.currencyconverter.inbound.controllers.resources

import com.fasterxml.jackson.annotation.JsonProperty
import com.gruposbf.currencyconverter.application.domain.Conversion
import com.gruposbf.currencyconverter.infra.repositories.resources.CurrencyEntity.CurrencyName.DOLLAR
import com.gruposbf.currencyconverter.infra.repositories.resources.CurrencyEntity.CurrencyName.EURO
import com.gruposbf.currencyconverter.infra.repositories.resources.CurrencyEntity.CurrencyName.INDIAN_RUPEE
import java.math.BigDecimal

data class ConvertResponse(
    @JsonProperty("USD")
    var usd: BigDecimal,
    @JsonProperty("EUR")
    var eur: BigDecimal,
    @JsonProperty("INR")
    var inr: BigDecimal,
) {
    constructor(conversions: List<Conversion>) : this(
        usd = conversions.find { it.currency.name == DOLLAR }!!.convertedAmount,
        eur = conversions.find { it.currency.name == EURO }!!.convertedAmount,
        inr = conversions.find { it.currency.name == INDIAN_RUPEE }!!.convertedAmount,
    )
}
