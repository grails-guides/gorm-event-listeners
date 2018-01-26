package demo

import grails.gorm.transactions.Rollback
import grails.gorm.transactions.Transactional
import org.grails.orm.hibernate.HibernateDatastore
import org.grails.testing.GrailsUnitTest
import org.springframework.transaction.PlatformTransactionManager
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class TagGenerationSpec  extends Specification implements GrailsUnitTest {

    @Shared
    @AutoCleanup
    HibernateDatastore hibernateDatastore
    @Shared
    PlatformTransactionManager transactionManager

    void setupSpec() {
        hibernateDatastore = applicationContext.getBean(HibernateDatastore)
        transactionManager = hibernateDatastore.getTransactionManager()
    }

    @Override
    Set<String> getIncludePlugins() {
        return ["eventBus"] as Set
    }


    @Override
    Closure doWithSpring() {
        { ->
            tagGenerationService TagGenerationService
            datastore(HibernateDatastore, [Book, Tag, BookTag])
        }
    }

    @Rollback
    @Transactional
    def "tag and bookTag are saved after book is saved"() {
        given:
        assert BookTag.count() == 0

        when:
        Book book = new Book(title: "ABC", author: "123", pages: 123).save(flush: true)
        then:
        BookTag.count() == 1

        and:
        BookTag bookTag = BookTag.first()

        bookTag.tag.name == book.title
        bookTag.book.id == book.id

    }

}
