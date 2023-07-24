package com.gruposbf.currencyconverter.application

import com.gruposbf.currencyconverter.application.domain.Conversion
import com.gruposbf.currencyconverter.application.domain.Currency
import com.gruposbf.currencyconverter.infra.repositories.CurrencyRepository
import com.gruposbf.currencyconverter.infra.repositories.resources.CurrencyEntity.CurrencyName
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@Service
class ConverterApplication(
    private val currencyRepository: CurrencyRepository,
    private val currenciesApplication: CurrenciesApplication
) {
    companion object {
        const val SCALE_LIMIT = 2
    }

    fun convert(amount: BigDecimal): List<Conversion> {
        val currencies = CurrencyName.values()
        val quotations = getQuotations(currencies)
        return quotations.map {
                currency ->
            Conversion(
                currency = currency,
                convertedAmount = calculate(amount, currency.quotation)
                    .setScale(SCALE_LIMIT, RoundingMode.CEILING)
            )
        }
    }

    private fun getQuotations(currencies: Array<CurrencyName>): List<Currency> {
        return currencies.map {
            currencyRepository.findByNameAndCreatedAt(it, LocalDate.now())?.toCurrency()
                ?: currenciesApplication.updateQuotation(it)
        }
    }

    private fun calculate(amount: BigDecimal, quotation: BigDecimal): BigDecimal {
        return amount.div(quotation)
    }
}
