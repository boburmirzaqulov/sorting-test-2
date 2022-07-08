package uz.springgroup.sortingtest2.exception;

import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.ValidatorDto;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlerResource{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidatorDto>> handleException(MethodArgumentNotValidException e, WebRequest request){
        List<ValidatorDto> list = new ArrayList<>();
        e.getBindingResult().getAllErrors()
                .forEach(er -> {
                    String fieldName = ((FieldError) er).getField();
                    String message = er.getDefaultMessage();
                    list.add(new ValidatorDto(fieldName, message));
                });
        return ResponseEntity.status(400).body(list);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ValidatorDto> handleDatabaseException(DatabaseException e){
        return ResponseEntity.status(400).body(new ValidatorDto(AppMessages.DATABASE_ERROR, e.getMessage()));
    }
}