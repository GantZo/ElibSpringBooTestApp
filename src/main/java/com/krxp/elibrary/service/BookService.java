package com.krxp.elibrary.service;

import com.krxp.elibrary.dto.BookDto;
import com.krxp.elibrary.enums.SortType;
import com.krxp.elibrary.model.Author;
import com.krxp.elibrary.model.Book;
import org.springframework.data.domain.Page;

import java.util.List;


public interface BookService {


    void addAuthor(Author author);
    void addBook(Book book, String authorSurname);

    void removeAuthor(String surname);
    void removeBook(String name);


    Book findByName(String name);

    Author findBySurname(String surname);


    Page<Author> findAllAuthors(final int page, final int size, final String field);

    Page<Book> findAllBooks(final int page, final int size, final String field);


    List<BookDto> getBooks(SortType sortType);

    List<BookDto> getAuthors();

    List<BookDto> getBooksByAuthor(String surname, SortType sortType);

    void clear();
}



