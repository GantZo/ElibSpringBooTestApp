package com.krxp.elibrary.controller;

public enum ReserveResponse {
    OK("Книга забронировна"),
    ERR_RESERVE("Книга недоступна для бронирования"),
    ERR_USER("Пользователь или книга не существует")

    ;

    final String responseMessage;
    ReserveResponse(final String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
