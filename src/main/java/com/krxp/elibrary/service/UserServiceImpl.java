package com.krxp.elibrary.service;

import com.krxp.elibrary.dao.BookDao;
import com.krxp.elibrary.dao.ReservationDao;
import com.krxp.elibrary.dao.UserDao;
import com.krxp.elibrary.dto.BookDto;
import com.krxp.elibrary.dto.UserDto;
import com.krxp.elibrary.enums.SortType;
import com.krxp.elibrary.model.Book;
import com.krxp.elibrary.model.Reservation;
import com.krxp.elibrary.model.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BookService bookService;

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private BookDao bookDao;

    private static final String SUCCESS_MESSAGE = "Книга забронировна";
    private static final String FAILED_RESERVE_MESSAGE = "Книга недоступна для бронирования до %s";
    private static final String FAILED_USER_MESSAGE = "Пользователь или книга не существует";

    @Override
    public void addUser(User user) {
        userDao.save(user);
    }

    @Override
    @Transactional
    public void removeUser(String login) {
        userDao.deleteByLogin(login);
    }

    @Override
    public Page<User> findAllUser(int page, int size, final String field) {
        return userDao.findAll(PageRequest.of(page, size, Sort.by(field).ascending()));
    }

    @Override
    @Transactional
    public Pair<Boolean, String> reserve(final String userLogin, final String bookName) {
        final User user = userDao.findByLogin(userLogin);
        final Book book = bookService.findByName(bookName);

        if (user != null && book != null) {

            if (book.getBooked() == null || Boolean.FALSE.equals(book.getBooked())) {
                book.setBooked(true);
                bookDao.save(book);
                final Reservation reservation = new Reservation();
                reservation.setUser(user);
                reservation.setBook(book);
                reservation.setBookedDate(LocalDateTime.now().plusHours(1));
                reservationDao.save(reservation);

                return Pair.of(Boolean.TRUE, SUCCESS_MESSAGE);
            } else {
                final Long bookId = book.getId();
                final Reservation reservation = reservationDao.findByBookId(bookId);

                return Pair.of(Boolean.FALSE, String.format(FAILED_RESERVE_MESSAGE, reservation.getBookedDate().toString()));
            }
        } else {
            return Pair.of(Boolean.FALSE, FAILED_USER_MESSAGE);
        }
    }

    @Override
    @Transactional
    public List<UserDto> getAllUsers(final SortType sortType) {
        return switch (sortType) {
            case REGISTRATION_DATE -> findAllUser(0, 5, "registrationDate")
                    .getContent()
                    .stream()
                    .map(user -> UserDto.from(user.getLogin(), user.getRegistrationDate()))
                    .toList();

            default -> findAllUser(0, 5, "login")
                    .getContent()
                    .stream()
                    .map(user -> UserDto.from(user.getLogin(), user.getRegistrationDate()))
                    .toList();
        };
    }

    @Override
    @Transactional
    public UserDto getUserInfo(final String userLogin) {
        final User user = userDao.findByLogin(userLogin);
        if (user != null) {
            final List<Reservation> reservation = reservationDao.findByUserId(user.getId());
            final List<BookDto> bookList = reservation
                    .stream()
                    .map(r -> BookDto
                            .builder()
                            .setBookName(r.getBook().getName())
                            .setPublishDate(r.getBook().getPublishYear())
                            .setAuthorName(r.getBook().getAuthor().getName())
                            .setAuthorSurname(r.getBook().getAuthor().getSurname())
                            .setReservedDate(r.getBookedDate())
                            .build())
                    .toList();

            return UserDto.from(user.getLogin(), user.getRegistrationDate(), bookList);
        } else {
            throw new EntityNotFoundException("This user does not exist");
        }
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

}
