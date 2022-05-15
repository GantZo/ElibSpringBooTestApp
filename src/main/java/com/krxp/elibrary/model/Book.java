package com.krxp.elibrary.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PUBLISH_YEAR")
    private LocalDate publishYear;

    @Column(name = "IS_BOOKED")
    private Boolean isBooked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR_ID")
    private Author author;

    public Book() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(LocalDate publishYear) {
        this.publishYear = publishYear;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Boolean getBooked() {
        return isBooked;
    }

    public void setBooked(Boolean booked) {
        isBooked = booked;
    }
}
