package demo

import grails.gorm.transactions.Rollback
import grails.gorm.transactions.Transactional
import org.grails.orm.hibernate.HibernateDatastore
import org.grails.testing.GrailsUnitTest
import org.springframework.transaction.PlatformTransactionManager
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class LoggingServiceSpec extends Specification implements GrailsUnitTest {

    @Shared @AutoCleanup HibernateDatastore hibernateDatastore
    @Shared PlatformTransactionManager transactionManager

    void setupSpec() {
        hibernateDatastore = applicationContext.getBean(HibernateDatastore)
        transactionManager = hibernateDatastore.getTransactionManager()
    }

    @Override
    Set<String> getIncludePlugins() {
        return ["eventBus"] as Set
    }


    @Override
    Closure doWithSpring() {{->
        loggingService(LoggingService)
        datastore(HibernateDatastore, Book)
    }}

    @Rollback
    @Transactional
    def "loggingService.afterSave is called after book is saved"() {
        given:
        LoggingService loggingService = applicationContext.getBean(LoggingService)

        when:
        new Book(title: "ABC", author: "123", pages: 123).save(flush: true)
        then:
        1 * loggingService.afterSave(_)
    }


}
