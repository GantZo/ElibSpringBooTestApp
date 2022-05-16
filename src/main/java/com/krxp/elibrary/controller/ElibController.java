package com.krxp.elibrary.controller;

import com.krxp.elibrary.dto.BookDto;
import com.krxp.elibrary.dto.UserDto;
import com.krxp.elibrary.enums.SortType;
import com.krxp.elibrary.service.BookService;
import com.krxp.elibrary.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/elib")
public class ElibController {

    private final BookService bookService;
    private final UserService userService;

    private final List<String> bookSortProperties = Arrays.asList("name", "publishYear");

    public ElibController(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("/books/list")
    public Page<BookDto> getBooksPage(Pageable pageable) {
        if (!bookSortProperties.containsAll(pageable.getSort().stream().map(Sort.Order::getProperty).toList())) {
            throw new RuntimeException("Sort properties are incorrect");
        }
        return bookService.getBooksPage(pageable);
    }

    @GetMapping("/books/get")
    public ResponseEntity<BookDto> getBook(@RequestParam Long id) {
        throw new RuntimeException("There is no realization");
    }

    @PutMapping("/books/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookDto> addBook(@RequestBody BookDto bookDto) {
        throw new RuntimeException("There is no realization");
    }

    @PostMapping("/books/edit")
    public ResponseEntity<BookDto> editBook(@RequestBody BookDto bookDto) {
        throw new RuntimeException("There is no realization");
    }

    @DeleteMapping("/books/delete/{id}")
    public void deleteBook(@PathVariable Long id) {
        throw new RuntimeException("There is no realization");
    }

    @GetMapping("/authors")
    public List<BookDto> getAuthors() {
        if (true) throw new RuntimeException("There is no authors list realization");
        // TODO это не похоже на список авторов, и на список книг
        return bookService.getAuthors();
    }

    // TODO лишнее если реализовать пагинацию
    @GetMapping("/author/books/sortByName")
    @ResponseBody
    public List<BookDto> getBooksByAuthorAndSortByName(@RequestParam final String authorSurname) {
        return bookService.getBooksByAuthor(authorSurname, SortType.BOOK_NAME);
    }

    // TODO лишнее если реализовать пагинацию
    @GetMapping("/author/books/sortByDate")
    @ResponseBody
    public List<BookDto> getBooksByAuthorAndSortBy(@RequestParam final String authorSurname) {
        return bookService.getBooksByAuthor(authorSurname, SortType.PUBLISH_DATE);
    }

    // TODO лишнее если реализовать пагинацию
    @GetMapping("/users/sortByLogin")
    @ResponseBody
    public List<UserDto> getAllUsersSortByLogin() {
        return userService.getAllUsers(SortType.USER_LOGIN);
    }

    // TODO лишнее если реализовать пагинацию
    @GetMapping("/users/sortByDate")
    @ResponseBody
    public List<UserDto> getAllUsersSortByDate() {
        return userService.getAllUsers(SortType.REGISTRATION_DATE);
    }

    @GetMapping("/books/reserve")
    @ResponseBody
    public String reserveBook(@RequestParam final String userLogin,
                              @RequestParam final String bookName) {
        // TODO дубликаты названий книг, например "Избранные стихотворения" публикации разных авторов
        // ну и этим не юзер сервис должен записаться
        return userService.reserve(userLogin, bookName).getResponseMessage();
    }

    // TODO лучше возвращать именно книги, т.к. информацию про себя можно и отдельно запросить
    @GetMapping("/user/info")
    @ResponseBody
    public UserDto reserveBook(@RequestParam final String userLogin) {
        return userService.getUserInfo(userLogin);
    }

}
