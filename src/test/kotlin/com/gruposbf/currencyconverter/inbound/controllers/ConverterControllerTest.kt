package com.gruposbf.currencyconverter.inbound.controllers

import com.gruposbf.currencyconverter.application.ConverterApplication
import com.gruposbf.currencyconverter.application.builders.ConversionBuilder
import com.gruposbf.currencyconverter.application.builders.CurrencyBuilder
import com.gruposbf.currencyconverter.inbound.controllers.resources.ConvertResponse
import com.gruposbf.currencyconverter.infra.repositories.resources.CurrencyEntity
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.math.BigDecimal

@ExtendWith(MockKExtension::class)
class ConverterControllerTest {

    @MockK
    private lateinit var converterApplication: ConverterApplication

    @InjectMockKs
    private lateinit var controller: ConverterController

    @Test
    fun `calls converter application`() {
        val amount = BigDecimal.valueOf(100)

        val dollarCurrency = CurrencyBuilder().apply {
            this.name = CurrencyEntity.CurrencyName.DOLLAR
        }.build()
        val euroCurrency = CurrencyBuilder().apply {
            this.name = CurrencyEntity.CurrencyName.EURO
        }.build()
        val rupeeCurrency = CurrencyBuilder().apply {
            this.name = CurrencyEntity.CurrencyName.INDIAN_RUPEE
        }.build()
        val dollarConversion = ConversionBuilder().apply {
            this.currency = dollarCurrency
        }.build()

        val euroConversion = ConversionBuilder().apply {
            this.currency = euroCurrency
        }.build()

        val rupeeConversion = ConversionBuilder().apply {
            this.currency = rupeeCurrency
        }.build()

        val expectedResponse = ResponseEntity(
            ConvertResponse(
                usd = dollarConversion.convertedAmount,
                eur = euroConversion.convertedAmount,
                inr = rupeeConversion.convertedAmount,
            ), HttpStatus.OK
        )

        every { converterApplication.convert(amount) } returns listOf(dollarConversion, euroConversion, rupeeConversion)

        val result = controller.convert(amount)

        assertThat(result).isEqualTo(expectedResponse)
    }
}
