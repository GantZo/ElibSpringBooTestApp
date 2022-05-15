package com.krxp.elibrary.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookDto {

    private String bookName;

    private LocalDate publishDate;

    private String authorName;

    private String authorSurname;

    private boolean isBooked;

    private String booked;

    private LocalDateTime reservedDate;

    private BookDto() {}

    public static BookBuilder builder() {
        return new BookDto().new BookBuilder();
    }

    public String getBookName() {
        return bookName;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorSurname() {
        return authorSurname;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public String getBooked() {
        return booked;
    }

    public LocalDateTime getReservedDate() {
        return reservedDate;
    }

    public class BookBuilder {

        private BookBuilder() {}

        public BookDto build() {
            return BookDto.this;
        }

        public BookBuilder setBookName(final String bookName) {
            BookDto.this.bookName = bookName;
            return this;
        }

        public BookBuilder setPublishDate(final LocalDate publishDate) {
            BookDto.this.publishDate = publishDate;
            return this;
        }

        public BookBuilder setAuthorName(final String authorName) {
            BookDto.this.authorName = authorName;
            return this;
        }

        public BookBuilder setAuthorSurname(final String authorSurname) {
            BookDto.this.authorSurname = authorSurname;
            return this;
        }

        public BookBuilder setIsBooked(final Boolean isBooked) {
            BookDto.this.isBooked = Boolean.TRUE.equals(isBooked);
            BookDto.this.booked = BookDto.this.isBooked ? "Книга недоступна" : "Книга свободна";
            return this;
        }

        public BookBuilder setReservedDate(final LocalDateTime reservedDate) {
            BookDto.this.reservedDate = reservedDate;
            return this;
        }
    }
}
