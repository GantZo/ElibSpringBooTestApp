package com.krxp.elibrary.service;

import com.krxp.elibrary.dao.AuthorDao;
import com.krxp.elibrary.dto.BookDto;
import com.krxp.elibrary.model.Author;
import com.krxp.elibrary.model.Book;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    public AuthorServiceImpl(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }


    @Override
    public void addAuthor(final Author author) {
        authorDao.save(author);
    }

    @Override
    @Transactional
    public void removeAuthor(final String surname) {
        throw new RuntimeException();
//        final Author author = authorDao.findBySurname(surname);
//        if (author != null) {
//            final List<Long> ids = author.getBooks().stream().map(Book::getId).toList();
//            bookDao.deleteAllById(ids);
//            authorDao.deleteBySurname(surname);
//        } else {
//            throw new EntityNotFoundException("This author does not exist");
//        }
    }

    @Override
    public Author findBySurname(final String surname) {
        return authorDao.findBySurname(surname);
    }

    @Override
    public Page<Author> findAllAuthors(final int page, final int size, final String field) {
        return authorDao.findAll(PageRequest.of(page, size, Sort.by(field).ascending()));
    }

    @Override
    @Transactional
    public List<BookDto> getAuthors() {
        final Page<Author> authors = findAllAuthors(0, 5, "surname");
        return authors
                .getContent()
                .stream()
                .map(author -> BookDto
                        .builder()
                        .setAuthorName(author.getName())
                        .setAuthorSurname(author.getSurname())
                        .build())
                .toList();
    }


}
