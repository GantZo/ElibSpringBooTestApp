package com.krxp.elibrary.service;

import com.krxp.elibrary.dao.AuthorDao;
import com.krxp.elibrary.dao.BookDao;
import com.krxp.elibrary.dto.BookDto;
import com.krxp.elibrary.enums.SortType;
import com.krxp.elibrary.model.Author;
import com.krxp.elibrary.model.Book;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final AuthorDao authorDao;
    private final BookDao bookDao;

    public BookServiceImpl(AuthorDao authorDao, BookDao bookDao) {
        this.authorDao = authorDao;
        this.bookDao = bookDao;
    }


    @Override
    @Transactional
    public void addBook(final Book book, final String authorSurname) {
        // TODO однофамильцы все испортят, лучше по id автора, но логика связи book-author изначально не правильная
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
    public void removeBook(final String name) {
        // TODO не безопасно, т.к. могут быть книги с одинаковым названием
        bookDao.deleteByName(name);
    }

    @Override
    public Optional<Book> findByName(final String name) {
        // TODO хоть и переделано на Optional, но опять же - могут быть дубли, и достанет не то что ожидаешь, лучше по id или возвращать List/Slice
        return bookDao.findFirstByName(name);
    }

    @Override
    public Page<Book> findAllBooks(final int page, final int size, final String field) {
        return bookDao.findAll(PageRequest.of(page, size, Sort.by(field).ascending()));
    }

    @Override
    public Page<BookDto> getBooksPage(Pageable pageable) {
        Page<Book> booksPage = bookDao.findAll(pageable);
        List<BookDto> bookDtoList = booksPage.stream().map(book -> BookDto.builder()
                        .setBookName(book.getName())
                        .setAuthorName(book.getAuthor().getName())
                        .setAuthorSurname(book.getAuthor().getSurname())
                        .setPublishDate(book.getPublishYear())
                        .setIsBooked(book.getBooked())
                        .build())
                .toList();
        return new PageImpl<>(bookDtoList, booksPage.getPageable(), booksPage.getTotalElements());
    }

    @Override
    @Transactional
    public List<BookDto> getBooks(final SortType sortType) {
//        return switch (sortType) {
//            case BOOK_NAME -> getBooks("name");
//            case PUBLISH_DATE -> getBooks("publishYear");
//            default -> getBooks("id");
//        };
        // TODO чтобы не вырезать тесты, а их переделать в дальнейшем
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public List<BookDto> getAuthors() {
        throw new RuntimeException();
//        final Page<Author> authors = findAllAuthors(0, 5, "surname");
//        return authors
//                .getContent()
//                .stream()
//                .map(author -> BookDto
//                        .builder()
//                        .setAuthorName(author.getName())
//                        .setAuthorSurname(author.getSurname())
//                        .build())
//                .toList();
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
        // TODO не раелизованно удаление,
//        bookDao.deleteAll();
//        authorDao.deleteAll();
    }
}
