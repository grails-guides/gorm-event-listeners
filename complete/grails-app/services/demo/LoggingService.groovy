package demo

import grails.events.annotation.gorm.Listener
import grails.gorm.transactions.Transactional
import groovy.util.logging.Slf4j
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import org.grails.datastore.mapping.engine.event.PostUpdateEvent
import org.grails.datastore.mapping.engine.event.PreDeleteEvent

@Slf4j
@Transactional
class LoggingService {

    @Listener
    void afterSave(PostInsertEvent event) {
        println "afterSave..."
        if (event.entityObject instanceof Book) {
            log.info "After book save..."
            new Book(event: "Book saved", bookId: ((Book) event.entityObject).id).save()
        }
    }

    @Listener
    void afterUpdate(PostUpdateEvent event) {
        if (event.entityObject instanceof Book) {
            log.info "After book update..."
            new Book(event: "Book saved", bookId: ((Book) event.entityObject).id).save()
        }
    }

    @Listener
    void beforeDelete(PreDeleteEvent event) {
        if (event.entityObject instanceof Book) {
            log.info "Before book delete..."
            new Book(event: "Book saved", bookId: ((Book) event.entityObject).id).save()
        }
    }
}
