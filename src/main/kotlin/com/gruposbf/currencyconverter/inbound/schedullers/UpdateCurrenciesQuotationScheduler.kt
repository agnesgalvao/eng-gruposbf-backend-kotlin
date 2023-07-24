package com.gruposbf.currencyconverter.inbound.schedullers

import com.gruposbf.currencyconverter.application.CurrenciesApplication
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class UpdateCurrenciesQuotationScheduler(
    private val currenciesApplication: CurrenciesApplication
) {
    private val logger = LoggerFactory.getLogger(UpdateCurrenciesQuotationScheduler::class.java)

    @Scheduled(cron = "0 0 6 * * *", zone = "America/Sao_Paulo")
    fun updateCurrenciesValuesTask() {
        logger.info("starting update currencies quotations")
        currenciesApplication.updateQuotations()
        logger.info("finish update currencies quotations")
    }
}
