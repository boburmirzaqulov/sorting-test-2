package uz.springgroup.sortingtest2.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import uz.springgroup.sortingtest2.dto.ValidatorDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidationMessage {
    private List<ValidatorDto> data;
}
