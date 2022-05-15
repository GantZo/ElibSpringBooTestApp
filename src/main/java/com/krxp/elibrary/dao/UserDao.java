package com.krxp.elibrary.dao;

import com.krxp.elibrary.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    User findByLogin(String login);

    void deleteByLogin(String login);

    @Override
    Page<User> findAll(Pageable pageable);
}
