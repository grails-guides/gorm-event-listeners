package demo

import grails.gorm.transactions.Transactional
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import spock.lang.Specification

class LoggingServiceUnitSpec extends Specification implements ServiceUnitTest<LoggingService>, DataTest {

    void setupSpec() {
        mockDomains Book, Audit
    }

    @Transactional
    void "test after save"(){
        given:
        Book book = new Book(title: 'abc', author: 'abc', pages: 1).save()
        PostInsertEvent event = new PostInsertEvent(dataStore, book)

        when:
        service.afterSave(event)

        then:
        Audit.count() == 1

        and:
        Audit audit = Audit.first()

        audit.event == "Book saved"
        audit.bookId == book.id
    }
}
