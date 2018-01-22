package demo

import groovy.util.logging.Slf4j
import org.grails.datastore.mapping.core.Datastore
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEventListener
import org.grails.datastore.mapping.engine.event.EventType
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import org.grails.datastore.mapping.engine.event.PostUpdateEvent
import org.grails.datastore.mapping.engine.event.PreUpdateEvent
import org.springframework.context.ApplicationEvent

@Slf4j
class TagGenerationListener extends AbstractPersistenceEventListener {

    TagGenerationListener(final Datastore datastore) {
        super(datastore)
    }

    @Override
    protected void onPersistenceEvent(AbstractPersistenceEvent event) {
        if (event.entityObject instanceof Book) {
            log.info "Saving book..."
            Book book = (Book) event.entityObject
            if (afterSave(event)) {
                log.info "afterSave..."

                Tag tag = Tag.findOrCreateWhere(name: book.title)
                tag.save()
                if(!BookTag.findByBookAndTag(book, tag)) {
                    BookTag.create(book, tag)
                }
            }

            if(beforeUpdate(event)) {
                log.info "beforeUpdate..."
                if(book.isDirty('title')) {
                    log.info "book.title dirty..."
                    Tag tag = Tag.findOrCreateWhere(name: book.getPersistentValue('title'))
                    tag.save()
                    BookTag bookTag = BookTag.findByBookAndTag(book, tag)
                    if(bookTag) bookTag.delete()
                }
            }
        }
    }

    private static afterSave(AbstractPersistenceEvent event ) {
        return [EventType.PostInsert, EventType.PostUpdate].contains(event.eventType)
    }

    private static beforeUpdate(AbstractPersistenceEvent event ) {
        return event.eventType == EventType.PreUpdate
    }

    @Override
    boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        [PostInsertEvent, PostUpdateEvent, PreUpdateEvent].contains(eventType)
    }

}
