package com.krxp.elibrary.dao;

import com.krxp.elibrary.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AuthorDao extends JpaRepository<Author, Long> {

    Author findBySurname(String surname);

    void deleteBySurname(String surname);

    @Override
    Page<Author> findAll(Pageable pageable);

}
