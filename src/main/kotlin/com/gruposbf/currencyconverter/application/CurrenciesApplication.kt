package com.gruposbf.currencyconverter.application

import com.gruposbf.currencyconverter.application.domain.Currency
import com.gruposbf.currencyconverter.infra.gateways.AwesomeApiGateway
import com.gruposbf.currencyconverter.infra.gateways.exceptions.ClientException
import com.gruposbf.currencyconverter.infra.repositories.CurrencyRepository
import com.gruposbf.currencyconverter.infra.repositories.resources.CurrencyEntity
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.rmi.UnexpectedException
import java.time.LocalDate

@Service
class CurrenciesApplication(
    private val awesomeApiGateway: AwesomeApiGateway,
    private val currencyRepository: CurrencyRepository
) {
    private val logger = LoggerFactory.getLogger(CurrenciesApplication::class.java)

    fun updateQuotations() {
        val currencies = CurrencyEntity.CurrencyName.values()
        currencies.forEach {
            currencyRepository.findByNameAndCreatedAt(it, LocalDate.now())
                ?: try {
                    awesomeApiGateway.getLastValue(it.code).let {
                            lastQuotation -> currencyRepository.save(
                            CurrencyEntity(
                                name = it,
                                quotation = lastQuotation.first().low.toBigDecimal(),
                            ))
                    }
                } catch (exception: UnexpectedException) {
                    logger.error("error: $exception")
                } catch (exception: ClientException) {
                    logger.error("error: $exception")
                }
        }
    }
    fun updateQuotation(currencyName: CurrencyEntity.CurrencyName): Currency {
        currencyRepository
        return awesomeApiGateway.getLastValue(
            currencyName.code
        ).let {
            val currency = CurrencyEntity(
                currencyName,
                it.first().low.toBigDecimal()
            )
            currencyRepository.save(currency).toCurrency()
        }
    }
}
