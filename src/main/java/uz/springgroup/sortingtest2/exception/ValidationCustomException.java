package uz.springgroup.sortingtest2.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import uz.springgroup.sortingtest2.dto.ValidatorDto;

import java.util.List;

public class ValidationCustomException extends RuntimeException {
    public ValidationCustomException(String message) {
        super(message);
    }

    public ValidationCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
