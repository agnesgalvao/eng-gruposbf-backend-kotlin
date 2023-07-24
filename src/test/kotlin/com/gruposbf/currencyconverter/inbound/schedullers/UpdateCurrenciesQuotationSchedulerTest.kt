package com.gruposbf.currencyconverter.inbound.schedullers

import com.gruposbf.currencyconverter.application.CurrenciesApplication
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class UpdateCurrenciesQuotationSchedulerTest {

    @MockK
    private lateinit var currenciesApplication: CurrenciesApplication

    @InjectMockKs
    private lateinit var updateCurrenciesValuesScheduler: UpdateCurrenciesQuotationScheduler

    @Test
    fun `calls currencies application`() {
        every { currenciesApplication.updateQuotations() } just runs

        updateCurrenciesValuesScheduler.updateCurrenciesValuesTask()

        verify(exactly = 1) { currenciesApplication.updateQuotations() }
    }
}
