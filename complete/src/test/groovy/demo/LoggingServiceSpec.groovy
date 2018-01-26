package demo

import grails.events.bus.EventBusBuilder
import grails.gorm.transactions.Rollback
import grails.gorm.transactions.Transactional
import org.grails.orm.hibernate.HibernateDatastore
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.transaction.PlatformTransactionManager
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class LoggingServiceSpec extends Specification {

    @Shared @AutoCleanup HibernateDatastore hibernateDatastore
    @Shared PlatformTransactionManager transactionManager

    void setupSpec() {
        println "setupSpec..."
        hibernateDatastore = new HibernateDatastore(Book)
        transactionManager = hibernateDatastore.getTransactionManager()
    }

    @Rollback
    @Transactional
    def "loggingService.afterSave is called after book is saved"() {
        given:
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext()
        applicationContext.beanFactory.registerSingleton("eventBus", new EventBusBuilder().build())
        applicationContext.beanFactory.registerSingleton("datastore", hibernateDatastore)
        applicationContext.beanFactory.registerSingleton("transactionManager", transactionManager)
        applicationContext.register(LoggingService)

        applicationContext.refresh()
        LoggingService loggingService = applicationContext.getBean(LoggingService)

        when:
        new Book(title: "ABC", author: "123", pages: 123).save(flush: true)
        then:
        1 * loggingService.afterSave(_)
    }


}
