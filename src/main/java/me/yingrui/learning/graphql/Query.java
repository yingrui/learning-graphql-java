package me.yingrui.learning.graphql;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import me.yingrui.learning.graphql.model.Book;

import java.util.List;

public class Query {

    private BookRepository bookRepository;

    public Query(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GraphQLQuery
    public Book getBookById(@GraphQLArgument(name = "id") String id) {
        return bookRepository.getBookById(id);
    }

    @GraphQLQuery
    public List<Book> search(@GraphQLArgument(name = "_q") String q) {
        return bookRepository.search(q, Book.class);
    }
}
