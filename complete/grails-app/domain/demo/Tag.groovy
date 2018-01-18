package demo

class Tag {

    String name

    Author author
    Book book

    static constraints = {
        author nullable: true
        book nullable: true
    }
}
