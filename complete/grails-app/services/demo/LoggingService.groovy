package demo

import grails.events.annotation.gorm.Listener
import grails.gorm.transactions.Transactional
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import org.grails.datastore.mapping.engine.event.PostUpdateEvent
import org.grails.datastore.mapping.engine.event.PreDeleteEvent


@Transactional
class LoggingService {

    @Listener(Book)
    void afterSave(PostInsertEvent event) {
        log.info "After book save..."
        new Audit(event: "Book saved", bookId: ((Book) event.entityObject).id).save()
    }

    @Listener(Book)
    void afterUpdate(PostUpdateEvent event) {
        log.info "After book update..."
        new Audit(event: "Book saved", bookId: ((Book) event.entityObject).id).save()
    }

    @Listener(Book)
    void beforeDelete(PreDeleteEvent event) {
        log.info "Before book delete..."
        new Audit(event: "Book saved", bookId: ((Book) event.entityObject).id).save()
    }
}
