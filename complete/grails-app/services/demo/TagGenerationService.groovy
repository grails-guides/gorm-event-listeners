package demo

import grails.events.annotation.gorm.Listener
import grails.gorm.transactions.Transactional
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import org.grails.datastore.mapping.engine.event.PostUpdateEvent
import org.grails.datastore.mapping.engine.event.PreDeleteEvent
import org.grails.datastore.mapping.engine.event.PreUpdateEvent

@Transactional
class TagGenerationService {

    @Listener(Book)
    void afterSave(PostInsertEvent event) {
        log.info "saveBookTag..."
        Book book = Book.load(((Book) event.entityObject).id)
        Tag tag = Tag.findOrCreateWhere(name: book.title).save()
        BookTag bookTag = BookTag.findByBookAndTag(book, tag)

        if (!bookTag) {
            log.info "Saving BookTag for $book & $tag"
            BookTag.create(book, tag)
        }
    }

    @Listener(Book)
    void afterUpdate(PostUpdateEvent event) {
        log.info "After book update..."

        Book book = Book.load(((Book) event.entityObject).id)
        Tag tag = Tag.findOrCreateWhere(name: book.title).save()
        BookTag bookTag = BookTag.findByBookAndTag(book, tag)

        if (!bookTag) {
            log.info "Saving BookTag for $book & $tag"
            BookTag.create(book, tag)
        }
    }

    @Listener(Book)
    void beforeUpdate(PreUpdateEvent event) {
        log.info "Before book update..."

        Book book = Book.load(((Book) event.entityObject).id)
        if (book.isDirty('title')) {
            log.info "book.title dirty..."
            Tag tag = Tag.findOrCreateWhere(name: book.getPersistentValue('title'))
            BookTag bookTag = BookTag.findByBookAndTag(book, tag)
            if (bookTag) bookTag.delete()
        }
    }

    @Listener(Book)
    void beforeDelete(PreDeleteEvent event) {
        log.info "Before book delete..."

        Book book = Book.load(((Book) event.entityObject).id)
        List<BookTag> bookTags = BookTag.findAllByBook(book)
        bookTags*.delete()
    }
}

