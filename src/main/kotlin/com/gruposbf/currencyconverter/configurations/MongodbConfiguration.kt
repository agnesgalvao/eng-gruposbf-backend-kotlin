package com.gruposbf.currencyconverter.configurations

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
class MongodbConfiguration(
    @Value("\${spring.data.mongodb.url}")
    private val url: String,
    @Value("\${spring.data.mongodb.database}")
    private val name: String

) {

    @Bean
    fun mongo(): MongoClient {
        val connectionString = ConnectionString(url)
        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build()
        return MongoClients.create(mongoClientSettings)
    }

    @Bean
    @Throws(Exception::class)
    fun mongoTemplate(): MongoTemplate {
        return MongoTemplate(mongo(), name)
    }
}
