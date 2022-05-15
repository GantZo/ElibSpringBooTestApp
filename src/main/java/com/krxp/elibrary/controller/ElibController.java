package com.krxp.elibrary.controller;

import com.krxp.elibrary.dto.BookDto;
import com.krxp.elibrary.dto.UserDto;
import com.krxp.elibrary.enums.SortType;
import com.krxp.elibrary.service.BookService;
import com.krxp.elibrary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/elib")
public class ElibController {

    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;


    @GetMapping("/books/sortByName")
    @ResponseBody
    public List<BookDto> getBooksSortByName() {
        return bookService.getBooks(SortType.BOOK_NAME);
    }

    @GetMapping("/books/sortByDate")
    @ResponseBody
    public List<BookDto> getBooksSortByDate() {
        return bookService.getBooks(SortType.PUBLISH_DATE);
    }

    @GetMapping("/authors")
    @ResponseBody
    public List<BookDto> getAuthors() {
        return bookService.getAuthors();
    }

    @GetMapping("/author/books/sortByName")
    @ResponseBody
    public List<BookDto> getBooksByAuthorAndSortByName(@RequestParam final String authorSurname) {
        return bookService.getBooksByAuthor(authorSurname, SortType.BOOK_NAME);
    }

    @GetMapping("/author/books/sortByDate")
    @ResponseBody
    public List<BookDto> getBooksByAuthorAndSortBy(@RequestParam final String authorSurname) {
        return bookService.getBooksByAuthor(authorSurname, SortType.PUBLISH_DATE);
    }

    @GetMapping("/users/sortByLogin")
    @ResponseBody
    public List<UserDto> getAllUsersSortByLogin() {
        return userService.getAllUsers(SortType.USER_LOGIN);
    }

    @GetMapping("/users/sortByDate")
    @ResponseBody
    public List<UserDto> getAllUsersSortByDate() {
        return userService.getAllUsers(SortType.REGISTRATION_DATE);
    }

    @GetMapping("/books/reserve")
    @ResponseBody
    public String reserveBook(@RequestParam final String userLogin, final String bookName) {
        return userService.reserve(userLogin, bookName).getResponseMessage();
    }

    @GetMapping("/user/info")
    @ResponseBody
    public UserDto reserveBook(@RequestParam final String userLogin) {
        return userService.getUserInfo(userLogin);
    }

}
