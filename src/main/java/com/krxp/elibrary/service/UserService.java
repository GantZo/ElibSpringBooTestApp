package com.krxp.elibrary.service;

import com.krxp.elibrary.controller.ReserveResponse;
import com.krxp.elibrary.dto.UserDto;
import com.krxp.elibrary.enums.SortType;
import com.krxp.elibrary.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    void addUser(User user);

    void removeUser(String login);

    Page<User> findAllUser(int page, int size, String login);

    void deleteAll();

    ReserveResponse reserve(String userLogin, String bookName);

    List<UserDto> getAllUsers(SortType sortType);

    UserDto getUserInfo(String userName);

}
