package uz.springgroup.sortingtest2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto <T>{
    private boolean success;
    private Integer code;
    private String message;
    private T data;
    private List<ValidatorDto> errors;

    public ResponseDto(boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
