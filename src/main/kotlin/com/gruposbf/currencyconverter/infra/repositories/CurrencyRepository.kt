package com.gruposbf.currencyconverter.infra.repositories

import com.gruposbf.currencyconverter.infra.repositories.resources.CurrencyEntity
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDate

interface CurrencyRepository : MongoRepository<CurrencyEntity, String> {
    fun findByNameAndCreatedAt(name: CurrencyEntity.CurrencyName, createdAt: LocalDate): CurrencyEntity?
}
