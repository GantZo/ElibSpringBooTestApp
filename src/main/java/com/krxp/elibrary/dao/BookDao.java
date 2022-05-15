package com.krxp.elibrary.dao;

import com.krxp.elibrary.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookDao extends JpaRepository<Book, Long> {

    Book findByName(String name);

    void deleteByName(String name);

    void deleteByIdIn(List<Long> ids);

    @Override
    Page<Book> findAll(Pageable pageable);

}
