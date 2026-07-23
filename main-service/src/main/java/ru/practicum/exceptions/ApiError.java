package ru.practicum.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private List<String> errors;
    private HttpStatus status;
    private String reason;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ApiError(HttpStatus status, String reason, String message, LocalDateTime timestamp) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }


}
