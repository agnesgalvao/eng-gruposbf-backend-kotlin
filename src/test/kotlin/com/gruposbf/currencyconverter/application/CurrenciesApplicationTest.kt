package com.gruposbf.currencyconverter.application

import com.gruposbf.currencyconverter.infra.gateways.AwesomeApiGateway
import com.gruposbf.currencyconverter.infra.gateways.exceptions.ClientException
import com.gruposbf.currencyconverter.infra.gateways.resources.AwesomeApiResponse
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
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.rmi.UnexpectedException
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
class CurrenciesApplicationTest {

    @MockK
    private lateinit var awesomeApiGateway: AwesomeApiGateway

    @MockK
    private lateinit var currencyRepository: CurrencyRepository

    @InjectMockKs
    private lateinit var currenciesApplication: CurrenciesApplication

    private val message = "error message"
    private val unexpectedException = UnexpectedException(message)
    private val clientException = ClientException(message)

    @Test
    fun `calls awesome api gateway and save currencies quotations`() {
        val dollarEntity = CurrencyEntityBuilder().apply {
            this.name = DOLLAR
        }.build()
        val euroEntity = CurrencyEntityBuilder().apply {
            this.name = EURO
        }.build()
        val rupeeEntity = CurrencyEntityBuilder().apply {
            this.name = INDIAN_RUPEE
        }.build()

        val awesomeApiResponse = AwesomeApiResponse(BigDecimal.TEN.toString())

        every { awesomeApiGateway.getLastValue(DOLLAR.code) } returns listOf(awesomeApiResponse)
        every { awesomeApiGateway.getLastValue(EURO.code) } returns listOf(awesomeApiResponse)
        every { awesomeApiGateway.getLastValue(INDIAN_RUPEE.code) } returns listOf(awesomeApiResponse)
        every { currencyRepository.findByNameAndCreatedAt(any(), any()) } returns null
        every { currencyRepository.save(dollarEntity) } returns dollarEntity
        every { currencyRepository.save(euroEntity) } returns euroEntity
        every { currencyRepository.save(rupeeEntity) } returns rupeeEntity

        currenciesApplication.updateQuotations()

        verify(exactly = 3) { awesomeApiGateway.getLastValue(any()) }
        verify(exactly = 3) { currencyRepository.save(any()) }
    }

    @Test
    fun `does not call awesome api gateway and save currencies quotations`() {
        val dollarEntity = CurrencyEntityBuilder().apply {
            this.name = DOLLAR
        }.build()
        val euroEntity = CurrencyEntityBuilder().apply {
            this.name = EURO
        }.build()
        val rupeeEntity = CurrencyEntityBuilder().apply {
            this.name = INDIAN_RUPEE
        }.build()

        every { currencyRepository.findByNameAndCreatedAt(DOLLAR, LocalDate.now()) } returns dollarEntity
        every { currencyRepository.findByNameAndCreatedAt(EURO, LocalDate.now()) } returns euroEntity
        every { currencyRepository.findByNameAndCreatedAt(INDIAN_RUPEE, LocalDate.now()) } returns rupeeEntity

        currenciesApplication.updateQuotations()

        verify(exactly = 0) { awesomeApiGateway.getLastValue(any()) }
        verify(exactly = 0) { currencyRepository.save(any()) }
    }

    @Test
    fun `calls awesome api gateway and save currency quotation`() {
        val currencyEntity = CurrencyEntityBuilder().build()
        val quotation = AwesomeApiResponse(BigDecimal.TEN.toString())
        every { currencyRepository.findByNameAndCreatedAt(any(), any()) } returns null
        every { awesomeApiGateway.getLastValue(currencyEntity.name.code) } returns listOf(quotation)
        every { currencyRepository.save(currencyEntity) } returns currencyEntity

        currenciesApplication.updateQuotation(currencyEntity.name)

        verify(exactly = 1) { awesomeApiGateway.getLastValue(currencyEntity.name.code) }
        verify(exactly = 1) { currencyRepository.save(currencyEntity) }
    }

    @Test
    fun `does not throws when try update quotations and awesome api throws client exception`() {
        every { awesomeApiGateway.getLastValue(any()) } throws clientException
        every { currencyRepository.findByNameAndCreatedAt(any(), any()) } returns null

        assertDoesNotThrow { currenciesApplication.updateQuotations() }
    }

    @Test
    fun `does not throws when try update quotations and awesome api throws unexpected exception`() {
        every { awesomeApiGateway.getLastValue(any()) } throws unexpectedException
        every { currencyRepository.findByNameAndCreatedAt(any(), any()) } returns null

        assertDoesNotThrow { currenciesApplication.updateQuotations() }
    }

    @Test
    fun `throws when try update quotation and awesome api throws client exception`() {
        every { awesomeApiGateway.getLastValue(any()) } throws clientException
        every { currencyRepository.findByNameAndCreatedAt(any(), any()) } returns null

        assertThatThrownBy { currenciesApplication.updateQuotation(DOLLAR) }
            .isInstanceOf(ClientException::class.java)
    }

    @Test
    fun `throws when try update quotation and awesome api throws unexpected exception`() {
        every { awesomeApiGateway.getLastValue(any()) } throws unexpectedException
        every { currencyRepository.findByNameAndCreatedAt(any(), any()) } returns null

        assertThatThrownBy { currenciesApplication.updateQuotation(DOLLAR) }
            .isInstanceOf(UnexpectedException::class.java)
    }
}
