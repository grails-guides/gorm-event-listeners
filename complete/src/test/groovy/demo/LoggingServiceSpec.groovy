package demo

import grails.gorm.transactions.Transactional
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import spock.lang.Specification

class LoggingServiceSpec extends Specification implements ServiceUnitTest<LoggingService>, DataTest { //<1>

    void setupSpec() {
        mockDomains Book, Audit //<2>
    }

    @Transactional
    void "test after save"(){
        given:
        Book book = new Book(title: 'abc', author: 'abc', pages: 1).save() //<3>
        PostInsertEvent event = new PostInsertEvent(dataStore, book) //<4>

        when:
        service.afterSave(event) //<5>

        then:
        Audit.count() == 1 //<6>

        and:
        Audit audit = Audit.first()

        audit.event == "Book saved"
        audit.bookId == book.id
    }
}
