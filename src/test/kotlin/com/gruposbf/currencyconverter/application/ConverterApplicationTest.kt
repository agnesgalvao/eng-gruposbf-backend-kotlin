package com.gruposbf.currencyconverter.application

import com.gruposbf.currencyconverter.application.builders.ConversionBuilder
import com.gruposbf.currencyconverter.application.builders.CurrencyBuilder
import com.gruposbf.currencyconverter.infra.gateways.exceptions.ClientException
import com.gruposbf.currencyconverter.infra.repositories.CurrencyRepository
import com.gruposbf.currencyconverter.infra.repositories.resouces.CurrencyEntityBuilder
import com.gruposbf.currencyconverter.infra.repositories.resources.CurrencyEntity.CurrencyName.DOLLAR
import com.gruposbf.currencyconverter.infra.repositories.resources.CurrencyEntity.CurrencyName.EURO
import com.gruposbf.currencyconverter.infra.repositories.resources.CurrencyEntity.CurrencyName.INDIAN_RUPEE
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.rmi.UnexpectedException
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
class ConverterApplicationTest {
    @MockK
    private lateinit var currenciesApplication: CurrenciesApplication

    @MockK
    private lateinit var currencyRepository: CurrencyRepository

    @InjectMockKs
    private lateinit var converterApplication: ConverterApplication

    private val amount = BigDecimal.valueOf(100)
    private val message = "error message"

    @Test
    fun `returns a conversion list when all quotations exists`() {
        val createdAt = LocalDate.now()
        val dollarEntity = CurrencyEntityBuilder().apply {
            this.name = DOLLAR
        }.build()
        val euroEntity = CurrencyEntityBuilder().apply {
            this.name = EURO
        }.build()
        val rupeeEntity = CurrencyEntityBuilder().apply {
            this.name = INDIAN_RUPEE
        }.build()

        val dollarConversion = ConversionBuilder().apply {
            this.currency = dollarEntity.toCurrency()
        }.build()

        val euroConversion = ConversionBuilder().apply {
            this.currency = euroEntity.toCurrency()
        }.build()

        val rupeeConversion = ConversionBuilder().apply {
            this.currency = rupeeEntity.toCurrency()
        }.build()

        every { currencyRepository.findByNameAndCreatedAt(DOLLAR, createdAt) } returns dollarEntity
        every { currencyRepository.findByNameAndCreatedAt(EURO, createdAt) } returns euroEntity
        every { currencyRepository.findByNameAndCreatedAt(INDIAN_RUPEE, createdAt) } returns rupeeEntity

        val result = converterApplication.convert(amount)

        verify(exactly = 3) { currencyRepository.findByNameAndCreatedAt(any(), createdAt) }
        verify(exactly = 0) { currenciesApplication.updateQuotation(any()) }

        assertThat(result).contains(
            dollarConversion,
            euroConversion,
            rupeeConversion
        )
    }

    @Test
    fun `returns a conversion list when all quotations exists `() {
        val createdAt = LocalDate.now()
        val dollarCurrency = CurrencyBuilder().apply {
            this.name = DOLLAR
        }.build()
        val euroCurrency = CurrencyBuilder().apply {
            this.name = EURO
        }.build()
        val rupeeCurrency = CurrencyBuilder().apply {
            this.name = INDIAN_RUPEE
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

        every { currenciesApplication.updateQuotation(DOLLAR) } returns dollarCurrency
        every { currenciesApplication.updateQuotation(EURO) } returns euroCurrency
        every { currenciesApplication.updateQuotation(INDIAN_RUPEE) } returns rupeeCurrency
        every { currencyRepository.findByNameAndCreatedAt(any(), createdAt) } returns null

        val result = converterApplication.convert(amount)

        verify(exactly = 3) { currencyRepository.findByNameAndCreatedAt(any(), createdAt) }
        verify(exactly = 3) { currenciesApplication.updateQuotation(any()) }

        assertThat(result).contains(
            dollarConversion,
            euroConversion,
            rupeeConversion
        )
    }

    @Test
    fun `throws when try update quotation and currencies application throws client exception`() {
        val exception = ClientException(message)

        every { currenciesApplication.updateQuotation(any()) } throws exception
        every { currencyRepository.findByNameAndCreatedAt(any(), any()) } returns null

        Assertions.assertThatThrownBy { converterApplication.convert(amount) }
            .isInstanceOf(ClientException::class.java)
    }

    @Test
    fun `throws when try update quotation and and currencies application throws unexpected exception`() {
        val exception = UnexpectedException(message)

        every { currenciesApplication.updateQuotation(any()) } throws exception
        every { currencyRepository.findByNameAndCreatedAt(any(), any()) } returns null

        Assertions.assertThatThrownBy { converterApplication.convert(amount) }
            .isInstanceOf(UnexpectedException::class.java)
    }
}
