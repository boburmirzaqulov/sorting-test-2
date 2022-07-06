package uz.springgroup.sortingtest2.service;

import org.springframework.stereotype.Service;
import uz.springgroup.sortingtest2.dto.ValidatorDto;
import uz.springgroup.sortingtest2.helper.AppMessages;

import java.util.List;

@Service
public class ValidationService {
    public static void universityValid(Integer id, List<ValidatorDto> errors){
        if (id == null) errors.add(new ValidatorDto("id", AppMessages.EMPTY_FIELD));
        if (id < 0) errors.add(new ValidatorDto("id", AppMessages.INCORRECT_TYPE));
    }
}
