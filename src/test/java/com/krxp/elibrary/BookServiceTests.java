package com.krxp.elibrary;

import com.krxp.elibrary.dto.BookDto;
import com.krxp.elibrary.enums.SortType;
import com.krxp.elibrary.model.Author;
import com.krxp.elibrary.model.Book;
import com.krxp.elibrary.service.BookService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class BookServiceTests {

    @Autowired
    private BookService bookService;

    @BeforeEach
    public void fillTable() {
        final Author jO = new Author();
        jO.setName("Джордж");
        jO.setSurname("Оруэл");

        final Book _1984 = new Book();
        _1984.setName("1984");
        _1984.setPublishYear(LocalDate.of(1949, 6, 8));
        _1984.setAuthor(jO);

        final Book animalFarm = new Book();
        animalFarm.setName("Скотный двор");
        animalFarm.setPublishYear(LocalDate.of(1949, 8, 17));
        animalFarm.setAuthor(jO);

        jO.addBook(_1984);
        jO.addBook(animalFarm);

        final Author remark = new Author();
        remark.setName("Эрих Мария");
        remark.setSurname("Ремарк");

        final Book threeComrades = new Book();
        threeComrades.setName("Три товарища");
        threeComrades.setPublishYear(LocalDate.of(1936, 12, 1));
        threeComrades.setAuthor(remark);

        final Book arcDeTriomphe = new Book();
        arcDeTriomphe.setName("Триумфальная арка");
        arcDeTriomphe.setPublishYear(LocalDate.of(1945, 1, 1));
        arcDeTriomphe.setAuthor(remark);

        final Book borrowedLife = new Book();
        borrowedLife.setName("Жизнь взаймы");
        borrowedLife.setPublishYear(LocalDate.of(1961, 1, 1));
        borrowedLife.setAuthor(remark);

        remark.addBook(threeComrades);
        remark.addBook(arcDeTriomphe);
        remark.addBook(borrowedLife);

        final Author hemingway = new Author();
        hemingway.setName("Эрнест");
        hemingway.setSurname("Хемингуэй");

        final Book forWhomTheBellTolls = new Book();
        forWhomTheBellTolls.setName("По ком звонит колокол");
        forWhomTheBellTolls.setPublishYear(LocalDate.of(1940, 1, 1));
        forWhomTheBellTolls.setAuthor(hemingway);

        hemingway.addBook(forWhomTheBellTolls);

        bookService.addAuthor(jO);
        bookService.addAuthor(remark);
        bookService.addAuthor(hemingway);
    }

    @Test
    public void addBookTest() {
        final Book oldManAndSea = new Book();
        oldManAndSea.setName("Старик и море");
        oldManAndSea.setPublishYear(LocalDate.of(1951, 9, 1));
        bookService.addBook(oldManAndSea, "Хемингуэй");

        final Book result = bookService.findByName(oldManAndSea.getName());

        assertEquals(oldManAndSea.getName(), result.getName());
        assertEquals(oldManAndSea.getPublishYear(), result.getPublishYear());
    }

    @Test
    public void removeAuthorAndBookTest() {
        bookService.removeAuthor("Хемингуэй");

        final Author author = bookService.findBySurname("Хемингуэй");
        final Book book = bookService.findByName("По ком звонит колокол");

        assertNull(author);
        assertNull(book);

        bookService.removeBook("Скотный двор");

        final Book book1 = bookService.findByName("Скотный двор");
        assertNull(book1);
    }

    @Test
    public void pagingAndSortTest() {
        Page<Author> authors = bookService.findAllAuthors(0, 1, "surname");
        Page<Book> books = bookService.findAllBooks(0, 3, "name");

        assertEquals(1, authors.getPageable().getPageSize());
        assertEquals("Оруэл", authors.getContent().get(0).getSurname());

        assertEquals(3, books.getPageable().getPageSize());
        assertEquals("1984", books.getContent().get(0).getName());
        assertEquals("Жизнь взаймы", books.getContent().get(1).getName());
        assertEquals("По ком звонит колокол", books.getContent().get(2).getName());
    }

    @Test
    public void getAllAndSortTest() {
        final List<BookDto> nameSortList = bookService.getBooks(SortType.BOOK_NAME);
        final List<BookDto> publicSortList = bookService.getBooks(SortType.PUBLISH_DATE);
        final List<BookDto> findByAuthorNameSortList =
                bookService.getBooksByAuthor("Ремарк", SortType.BOOK_NAME);
        final List<BookDto> findByAuthorPublicSortList =
                bookService.getBooksByAuthor("Ремарк", SortType.PUBLISH_DATE);

        assertEquals(nameSortList.get(0).getBookName(), "1984");
        assertEquals(nameSortList.get(1).getBookName(), "Жизнь взаймы");
        assertEquals(nameSortList.get(2).getBookName(), "По ком звонит колокол");
        assertEquals(nameSortList.get(3).getBookName(), "Скотный двор");
        assertEquals(nameSortList.get(4).getBookName(), "Три товарища");

        assertEquals(publicSortList.get(0).getBookName(), "Три товарища");
        assertEquals(publicSortList.get(1).getBookName(), "По ком звонит колокол");
        assertEquals(publicSortList.get(2).getBookName(), "Триумфальная арка");
        assertEquals(publicSortList.get(3).getBookName(), "1984");
        assertEquals(publicSortList.get(4).getBookName(), "Скотный двор");

        assertEquals(findByAuthorNameSortList.get(0).getBookName(), "Жизнь взаймы");
        assertEquals(findByAuthorNameSortList.get(1).getBookName(), "Три товарища");
        assertEquals(findByAuthorNameSortList.get(2).getBookName(), "Триумфальная арка");
        assertEquals(findByAuthorNameSortList.get(0).getAuthorSurname(), "Ремарк");
        assertEquals(findByAuthorNameSortList.get(1).getAuthorSurname(), "Ремарк");
        assertEquals(findByAuthorNameSortList.get(2).getAuthorSurname(), "Ремарк");

        assertEquals(findByAuthorPublicSortList.get(0).getBookName(), "Три товарища");
        assertEquals(findByAuthorPublicSortList.get(1).getBookName(), "Триумфальная арка");
        assertEquals(findByAuthorPublicSortList.get(2).getBookName(), "Жизнь взаймы");
        assertEquals(findByAuthorPublicSortList.get(0).getAuthorSurname(), "Ремарк");
        assertEquals(findByAuthorPublicSortList.get(1).getAuthorSurname(), "Ремарк");
        assertEquals(findByAuthorPublicSortList.get(2).getAuthorSurname(), "Ремарк");
    }

    @AfterEach
    public void clear() {
        bookService.clear();
    }
}
