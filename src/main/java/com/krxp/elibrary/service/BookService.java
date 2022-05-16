package com.krxp.elibrary.service;

import com.krxp.elibrary.dto.BookDto;
import com.krxp.elibrary.enums.SortType;
import com.krxp.elibrary.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface BookService {

    void addBook(Book book, String authorSurname);

    void removeBook(String name);

    Optional<Book> findByName(String name);

    Page<Book> findAllBooks(final int page, final int size, final String field);


    List<BookDto> getBooks(SortType sortType);

    List<BookDto> getAuthors();

    List<BookDto> getBooksByAuthor(String surname, SortType sortType);

    void clear();

    Page<BookDto> getBooksPage(Pageable pageable);
}



