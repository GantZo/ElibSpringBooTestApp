package com.krxp.elibrary.dto;

import java.time.LocalDate;
import java.util.List;

public class UserDto {

    private final String userLogin;

    private final LocalDate registrationDate;

    private final List<BookDto> bookList;

    private UserDto(final String userLogin, final LocalDate registrationDate, final List<BookDto> bookList) {
        this.userLogin = userLogin;
        this.registrationDate = registrationDate;
        this.bookList = bookList;
    }

    public static UserDto from(final String userLogin, final LocalDate registrationDate) {
        return new UserDto(userLogin, registrationDate, null);
    }

    public static UserDto from(final String userLogin, final LocalDate registrationDate, final List<BookDto> bookList) {
        return new UserDto(userLogin, registrationDate, bookList);
    }

    public String getUserLogin() {
        return userLogin;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public List<BookDto> getBookList() {
        return bookList;
    }
}
