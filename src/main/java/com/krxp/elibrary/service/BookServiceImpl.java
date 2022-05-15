package com.krxp.elibrary.service;

import com.krxp.elibrary.dao.AuthorDao;
import com.krxp.elibrary.dao.BookDao;
import com.krxp.elibrary.dto.BookDto;
import com.krxp.elibrary.enums.SortType;
import com.krxp.elibrary.model.Author;
import com.krxp.elibrary.model.Book;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private BookDao bookDao;


    @Override
    public void addAuthor(final Author author) {
       authorDao.save(author);
    }

    @Override
    @Transactional
    public void addBook(final Book book, final String authorSurname) {
        final Author author = authorDao.findBySurname(authorSurname);
        if (author != null) {
            book.setAuthor(author);
            author.addBook(book);
            authorDao.save(author);
        } else {
            throw new EntityNotFoundException("This author does not exist");
        }
    }

    @Override
    @Transactional
    public void removeAuthor(final String surname) {
        final Author author = authorDao.findBySurname(surname);
        if (author != null) {
            final List<Long> ids = author.getBooks().stream().map(Book::getId).toList();
            bookDao.deleteByIdIn(ids);
            authorDao.deleteBySurname(surname);
        } else {
            throw new EntityNotFoundException("This author does not exist");
        }
    }

    @Override
    @Transactional
    public void removeBook(final String name) {
        bookDao.deleteByName(name);
    }

    @Override
    public Book findByName(final String name) {
        return bookDao.findByName(name);
    }

    @Override
    public Author findBySurname(final String surname) {
        return authorDao.findBySurname(surname);
    }

    @Override
    public Page<Author> findAllAuthors(final int page, final int size, final String field) {
        return authorDao.findAll(PageRequest.of(page,size, Sort.by(field).ascending()));
    }

    @Override
    public Page<Book> findAllBooks(final int page, final int size, final String field) {
        return bookDao.findAll(PageRequest.of(page,size, Sort.by(field).ascending()));
    }

    @Override
    @Transactional
    public List<BookDto> getBooks(final SortType sortType) {
        return switch (sortType) {
            case BOOK_NAME -> getBooks("name");
            case PUBLISH_DATE -> getBooks("publishYear");
            default -> getBooks("id");
        };
    }

    private List<BookDto> getBooks(final String sort) {
        return findAllBooks(0, 5, sort)
                .getContent()
                .stream()
                .map(book -> BookDto
                        .builder()
                        .setBookName(book.getName())
                        .setAuthorName(book.getAuthor().getName())
                        .setAuthorSurname(book.getAuthor().getSurname())
                        .setPublishDate(book.getPublishYear())
                        .setIsBooked(book.getBooked())
                        .build())
                .toList();
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

    @Override
    @Transactional
    public List<BookDto> getBooksByAuthor(final String surname, final SortType sortType) {
        final Author author = authorDao.findBySurname(surname);
        List<Book> books = author.getBooks();

        switch (sortType) {
            case BOOK_NAME -> books = books.stream().sorted(Comparator.comparing(Book::getName)).toList();
            case PUBLISH_DATE -> books = books.stream().sorted(Comparator.comparing(Book::getPublishYear)).toList();
        }

        return books
                .stream()
                .map(book -> BookDto
                        .builder()
                        .setBookName(book.getName())
                        .setAuthorName(book.getAuthor().getName())
                        .setAuthorSurname(book.getAuthor().getSurname())
                        .setPublishDate(book.getPublishYear())
                        .setIsBooked(book.getBooked())
                        .build())
                .toList();
    }

    @Override
    public void clear() {
        bookDao.deleteAll();
        authorDao.deleteAll();
    }
}
