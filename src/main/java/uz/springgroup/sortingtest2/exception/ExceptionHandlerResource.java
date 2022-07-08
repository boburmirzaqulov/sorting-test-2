package uz.springgroup.sortingtest2.exception;

import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import uz.springgroup.sortingtest2.dto.ValidatorDto;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlerResource{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidatorDto>> handleException(MethodArgumentNotValidException e, WebRequest request){
//        Map<String, String> errors = new HashMap<>();
        List<ValidatorDto> list = new ArrayList<>();
        e.getBindingResult().getAllErrors()
                .forEach(er -> {
                    String fieldName = ((FieldError) er).getField();
                    String message = er.getDefaultMessage();
                    list.add(new ValidatorDto(fieldName, message));
//                    errors.put(fieldName, message);
                });
        return ResponseEntity.status(400).body(list);
    }

    @ExceptionHandler(ValidationCustomException.class)
    public ResponseEntity<List<ValidatorDto>> handleValidationCustomException(ValidationCustomException e){
        ValidationMessage validationMessage = new Gson().fromJson(e.getMessage(),ValidationMessage.class);
        return ResponseEntity.status(400).body(validationMessage.getData());
    }
}