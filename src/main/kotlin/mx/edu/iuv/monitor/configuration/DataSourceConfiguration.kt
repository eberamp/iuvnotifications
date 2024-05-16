package mx.edu.iuv.monitor.configuration

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.hibernate.jpa.HibernatePersistenceProvider
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = [
        "mx.edu.iuv.monitor.infrastructure.output.database",
        "mx.edu.iuv.monitor.infrastructure.output.database.entity"],
    entityManagerFactoryRef = "iuvMoodleEntityManagerFactory",
    transactionManagerRef = "iuvMoodleTransactionManager"
)
class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.iuv")
    fun firstDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Primary
    @Bean(name = ["iuvMoodleDataSource"])
    @ConfigurationProperties(prefix = "spring.datasource.iuv")
    fun firstDataSource(properties: DataSourceProperties): HikariDataSource {
        return properties.initializeDataSourceBuilder().type(HikariDataSource::class.java).build()
    }

    @Primary
    @Bean(name = ["iuvMoodleEntityManagerFactory"])
    fun firstEntityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        return configureEntityManagerFactory(dataSource)
    }

    @Primary
    @Bean(name = ["iuvMoodleTransactionManager"])
    fun firstTransactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }

    @Bean
    @ConfigurationProperties("spring.datasource.notification-log")
    fun secondDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean(name = ["notificationLogDataSource"])
    @ConfigurationProperties(prefix = "spring.datasource.notification-log")
    fun secondDataSource(properties: DataSourceProperties): HikariDataSource {
        return properties.initializeDataSourceBuilder().type(HikariDataSource::class.java).build()
    }

    @Bean(name = ["notificationLogEntityManagerFactory"])
    fun secondEntityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        return configureEntityManagerFactory(dataSource)
    }


    @Bean(name = ["notificationLogTransactionManager"])
    fun secondTransactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }

    private fun configureEntityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val entityManagerFactoryBean = LocalContainerEntityManagerFactoryBean()
        entityManagerFactoryBean.dataSource = dataSource
        entityManagerFactoryBean.setPackagesToScan("mx.edu.iuv.monitor.infrastructure.output.database.entity")
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider::class.java)

        entityManagerFactoryBean.setJpaPropertyMap(mapOf(
            "hibernate.dialect" to "org.hibernate.dialect.MySQLDialect",
        ))

        return entityManagerFactoryBean
    }
}