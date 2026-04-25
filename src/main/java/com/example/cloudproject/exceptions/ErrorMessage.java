package com.example.cloudproject.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorMessage {
    private String message;
    private String status;
}
