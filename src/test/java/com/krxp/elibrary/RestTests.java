package com.krxp.elibrary;

import com.krxp.elibrary.enums.ReserveResponse;
import com.krxp.elibrary.dao.ReservationDao;
import com.krxp.elibrary.dao.UserDao;
import com.krxp.elibrary.model.Author;
import com.krxp.elibrary.model.Book;
import com.krxp.elibrary.model.Reservation;
import com.krxp.elibrary.model.User;
import com.krxp.elibrary.service.BookService;
import com.krxp.elibrary.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.CoreMatchers.is;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureMockMvc()
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class RestTests {

    @Autowired
    private MockMvc mockMvc;

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
        user1.setRegistrationDate(LocalDate.now().minusDays(4));

        final User user2 = new User();
        user2.setLogin("user2");
        user2.setRegistrationDate(LocalDate.now().minusDays(3));

        final User user3 = new User();
        user3.setLogin("user3");
        user3.setRegistrationDate(LocalDate.now().minusDays(2));

        final User user4 = new User();
        user4.setLogin("user4");
        user4.setRegistrationDate(LocalDate.now().minusDays(1));

        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        userService.addUser(user4);

        final Author jO = new Author();
        jO.setName("????????????");
        jO.setSurname("??????????");

        final Book _1984 = new Book();
        _1984.setName("1984");
        _1984.setPublishYear(LocalDate.of(1949, 6, 8));
        _1984.setAuthor(jO);

        final Book animalFarm = new Book();
        animalFarm.setName("?????????????? ????????");
        animalFarm.setPublishYear(LocalDate.of(1949, 8, 17));
        animalFarm.setAuthor(jO);

        jO.addBook(_1984);
        jO.addBook(animalFarm);

        final Author remark = new Author();
        remark.setName("???????? ??????????");
        remark.setSurname("????????????");

        final Book threeComrades = new Book();
        threeComrades.setName("?????? ????????????????");
        threeComrades.setPublishYear(LocalDate.of(1936, 12, 1));
        threeComrades.setAuthor(remark);

        final Book arcDeTriomphe = new Book();
        arcDeTriomphe.setName("???????????????????????? ????????");
        arcDeTriomphe.setPublishYear(LocalDate.of(1945, 1, 1));
        arcDeTriomphe.setAuthor(remark);

        final Book borrowedLife = new Book();
        borrowedLife.setName("?????????? ????????????");
        borrowedLife.setPublishYear(LocalDate.of(1961, 1, 1));
        borrowedLife.setAuthor(remark);

        remark.addBook(threeComrades);
        remark.addBook(arcDeTriomphe);
        remark.addBook(borrowedLife);

        final Author hemingway = new Author();
        hemingway.setName("????????????");
        hemingway.setSurname("??????????????????");

        final Book forWhomTheBellTolls = new Book();
        forWhomTheBellTolls.setName("???? ?????? ???????????? ??????????????");
        forWhomTheBellTolls.setPublishYear(LocalDate.of(1940, 1, 1));
        forWhomTheBellTolls.setAuthor(hemingway);

        hemingway.addBook(forWhomTheBellTolls);

        bookService.addAuthor(jO);
        bookService.addAuthor(remark);
        bookService.addAuthor(hemingway);
    }

    @Test
    public void getAllWithSortTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/elib/books/sortByName").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookName", is("1984")))
                .andExpect(jsonPath("$[1].bookName", is("?????????? ????????????")))
                .andExpect(jsonPath("$[2].bookName", is("???? ?????? ???????????? ??????????????")))
                .andExpect(jsonPath("$[3].bookName", is("?????????????? ????????")))
                .andExpect(jsonPath("$[4].bookName", is("?????? ????????????????")));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/elib/books/sortByDate").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookName", is("?????? ????????????????")))
                .andExpect(jsonPath("$[1].bookName", is("???? ?????? ???????????? ??????????????")))
                .andExpect(jsonPath("$[2].bookName", is("???????????????????????? ????????")))
                .andExpect(jsonPath("$[3].bookName", is("1984")))
                .andExpect(jsonPath("$[4].bookName", is("?????????????? ????????")));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/elib/authors").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].authorSurname", is("??????????")))
                .andExpect(jsonPath("$[1].authorSurname", is("????????????")))
                .andExpect(jsonPath("$[2].authorSurname", is("??????????????????")));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/elib/author/books/sortByName?authorSurname=????????????").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookName", is("?????????? ????????????")))
                .andExpect(jsonPath("$[1].bookName", is("?????? ????????????????")))
                .andExpect(jsonPath("$[2].bookName", is("???????????????????????? ????????")));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/elib/author/books/sortByDate?authorSurname=????????????").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookName", is("?????? ????????????????")))
                .andExpect(jsonPath("$[1].bookName", is("???????????????????????? ????????")))
                .andExpect(jsonPath("$[2].bookName", is("?????????? ????????????")));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/elib/users/sortByLogin").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userLogin", is("user1")))
                .andExpect(jsonPath("$[1].userLogin", is("user2")))
                .andExpect(jsonPath("$[2].userLogin", is("user3")));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/elib/users/sortByDate").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].registrationDate", is("2022-05-11")))
                .andExpect(jsonPath("$[1].registrationDate", is("2022-05-12")))
                .andExpect(jsonPath("$[2].registrationDate", is("2022-05-13")));
    }

    @Test
    public void reserveOperationTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/elib/books/reserve?userLogin=user1&bookName=1984")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(
                        result.getResponse().getContentAsString(),
                        ReserveResponse.OK.getResponseMessage()));

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/elib/books/reserve?userLogin=user1&bookName=1984")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(
                        result.getResponse().getContentAsString(),
                        ReserveResponse.ERR_RESERVE.getResponseMessage())
                );

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/elib/books/reserve?userLogin=user1&bookName=?????????? ????????????")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(
                        result.getResponse().getContentAsString(),
                        ReserveResponse.OK.getResponseMessage()));

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/elib/books/reserve?userLogin=user1&bookName=wqeqw")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(
                        result.getResponse().getContentAsString(),
                        ReserveResponse.ERR_USER.getResponseMessage()));


        final Book book1 = bookService.findByName("1984");
        final Reservation reservation1 = reservationDao.findByBookId(book1.getId());

        final Book book2 = bookService.findByName("?????????? ????????????");
        final Reservation reservation2 = reservationDao.findByBookId(book2.getId());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/elib/user/info?userLogin=user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("userLogin", is("user1")))
                .andExpect(jsonPath("bookList.[0].bookName", is("1984")))
                .andExpect(jsonPath("bookList.[1].bookName", is("?????????? ????????????")))
                .andExpect(jsonPath("bookList.[0].reservedDate", is(reservation1.getBookedDate().toString())))
                .andExpect(jsonPath("bookList.[1].reservedDate", is(reservation2.getBookedDate().toString())));
    }

    @AfterEach
    public void clear() {
        reservationDao.deleteAll();
        userDao.deleteAll();
        bookService.clear();
    }
}
