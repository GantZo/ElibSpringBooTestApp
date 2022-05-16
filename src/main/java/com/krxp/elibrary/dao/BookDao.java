package com.krxp.elibrary.dao;

import com.krxp.elibrary.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookDao extends JpaRepository<Book, Long> {

    Optional<Book> findFirstByName(String name);

    void deleteByName(String name);

    @Override
    Page<Book> findAll(Pageable pageable);

}
