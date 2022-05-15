package com.krxp.elibrary;

import com.krxp.elibrary.enums.ReserveResponse;
import com.krxp.elibrary.dao.ReservationDao;
import com.krxp.elibrary.dao.UserDao;
import com.krxp.elibrary.dto.UserDto;
import com.krxp.elibrary.enums.SortType;
import com.krxp.elibrary.model.Author;
import com.krxp.elibrary.model.Book;
import com.krxp.elibrary.model.User;
import com.krxp.elibrary.service.BookService;
import com.krxp.elibrary.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private UserDao userDao;

    @BeforeEach
    public void fillTable() {
        final User user1 = new User();
        user1.setLogin("user1");
        user1.setRegistrationDate(LocalDate.now());

        final User user2 = new User();
        user2.setLogin("user2");
        user2.setRegistrationDate(LocalDate.now().minusDays(2));

        final User user3 = new User();
        user3.setLogin("user3");
        user3.setRegistrationDate(LocalDate.now().minusDays(3));

        final User user4 = new User();
        user4.setLogin("user4");
        user4.setRegistrationDate(LocalDate.now().minusDays(4));

        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        userService.addUser(user4);

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
    public void removeUser() {
        userService.removeUser("user1");
        Page<User> users =  userService.findAllUser(0, 5, "login");
        assertEquals(users.getTotalElements(), 3);
    }

    @Test
    public void reserveTest() {
        final ReserveResponse result1 = userService.reserve("user1", "Триумфальная арка");
        final ReserveResponse result2 = userService.reserve("user1", "asrqwe42");
        final ReserveResponse result3 = userService.reserve("user1", "Триумфальная арка");

        assertEquals(result1.getResponseMessage(), ReserveResponse.OK.getResponseMessage());
        assertEquals(result2.getResponseMessage(), ReserveResponse.ERR_USER.getResponseMessage());
        assertEquals(result3.getResponseMessage(), ReserveResponse.ERR_RESERVE.getResponseMessage());
    }

    @Test
    public void getUsers() {
       final List<UserDto> user1 = userService.getAllUsers(SortType.USER_LOGIN);
       final List<UserDto> user2 = userService.getAllUsers(SortType.REGISTRATION_DATE);
       userService.reserve("user2", "1984");
       final UserDto userInfo = userService.getUserInfo("user2");

        assertEquals(user1.get(0).getUserLogin(), "user1");
        assertEquals(user1.get(1).getUserLogin(), "user2");
        assertEquals(user1.get(2).getUserLogin(), "user3");

        assertEquals(user2.get(0).getUserLogin(), "user4");
        assertEquals(user2.get(1).getUserLogin(), "user3");
        assertEquals(user2.get(2).getUserLogin(), "user2");

        assertEquals(userInfo.getBookList().get(0).getBookName(), "1984");
    }

    @AfterEach
    public void clear() {
        reservationDao.deleteAll();
        userDao.deleteAll();
        bookService.clear();
    }
}
