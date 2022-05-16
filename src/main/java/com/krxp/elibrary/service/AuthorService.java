package com.krxp.elibrary.service;

import com.krxp.elibrary.dto.BookDto;
import com.krxp.elibrary.model.Author;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuthorService {

    void addAuthor(Author author);
    void removeAuthor(String surname);
    Author findBySurname(String surname);
    Page<Author> findAllAuthors(final int page, final int size, final String field);

    List<BookDto> getAuthors();

}
