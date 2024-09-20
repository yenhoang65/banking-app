package com.example.banking_app.Exception;

import java.time.LocalDateTime;

public record ErrorDetail(LocalDateTime timestamp, String message, String details, String errorCode) {

}
