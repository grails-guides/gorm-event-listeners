package demo

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import org.grails.datastore.mapping.engine.event.PostUpdateEvent
import org.grails.datastore.mapping.engine.event.PreDeleteEvent
import org.grails.datastore.mapping.engine.event.PreInsertEvent
import org.grails.datastore.mapping.engine.event.PreUpdateEvent
import spock.lang.Specification

class TagGenerationServiceSpec extends Specification implements ServiceUnitTest<TagGenerationService>, DataTest { //<1>

    def setupSpec() {
        mockDomain Book
    }

    def "tag and bookTag are saved after book is saved"() { //<5>
        given:
        service.tagDataService = Mock(TagDataService)
        service.bookTagDataService = Mock(BookTagDataService)

        when:
        Book book = new Book(title: 'Practical Grails 3', author: "Eric Helgeson", pages: 123).save()
        service.afterInsert(new PostInsertEvent(dataStore, book))

        then:
        1 * service.tagDataService.find('Practical Grails 3')
        1 * service.tagDataService.save('Practical Grails 3')
        1 * service.bookTagDataService.findByBookIdAndTag(_, _)
        1 * service.bookTagDataService.save(_, _)
    }

    def "tag and bookTag are saved after book is update"() { //<5>
        given:
        service.tagDataService = Mock(TagDataService)
        service.bookTagDataService = Mock(BookTagDataService)

        when:
        Book book = new Book(title: 'Grails 3 Step by Step', author: "Cristian Olaru", pages: 50).save()
        service.afterUpdate(new PostUpdateEvent(dataStore, book))

        then:
        1 * service.tagDataService.find('Grails 3 Step by Step')
        1 * service.tagDataService.save('Grails 3 Step by Step')
        1 * service.bookTagDataService.findByBookIdAndTag(_, _)
        1 * service.bookTagDataService.save(_, _)
    }

    def "tags are fetched to be removed from book before the book is updated"() { //<5>
        given:
        service.bookTagDataService = Mock(BookTagDataService)

        when:
        Book book = new Book(title: 'Practical Grails 3', author: "Eric Helgeson", pages: 123).save()
        service.beforeUpdate(new PreUpdateEvent(dataStore, book))

        then:
        1 * service.bookTagDataService.findAllByBookId(_)
    }

    def "tags are fetched to be removed from book before the book is deleted"() { //<5>
        given:
        service.bookTagDataService = Mock(BookTagDataService)

        when:
        Book book = new Book(title: 'Practical Grails 3', author: "Eric Helgeson", pages: 123).save()
        service.beforeDelete(new PreDeleteEvent(dataStore, book))

        then:
        1 * service.bookTagDataService.findAllByBookId(_)
    }
}
