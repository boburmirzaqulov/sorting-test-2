package uz.springgroup.sortingtest2.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.ValidatorDto;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.helper.StringHelper;
import uz.springgroup.sortingtest2.service.ValidationService;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeneralService {

    public static void getAllGeneral(MultiValueMap<String, String> params, boolean isPage, boolean isSize, List<ValidatorDto> errors){
        isPage = StringHelper.isNumber(params.getFirst("page"));
        isSize = StringHelper.isNumber(params.getFirst("size"));
        if (isPage) errors.add(new ValidatorDto("page", AppMessages.NOT_FOUND));
        if (isSize) errors.add(new ValidatorDto("size", AppMessages.NOT_FOUND));
    }

    public static ResponseDto<Integer> deleteGeneral(JpaRepository repository, Integer id){
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.universityValid(id, errors);
        if (!errors.isEmpty()) {
            new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        }
        if (!repository.existsById(id)){
            new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
        }
        repository.deleteById(id);
        return new ResponseDto<>(true, AppCode.OK, AppMessages.DELETED, id);
    }

    public static ResponseDto<?> updateGeneral(JpaRepository repository, Integer id){
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.universityValid(id, errors);
        if (!errors.isEmpty()) {
            new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        }
        if (!repository.existsById(id)){
            return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, id);
        }
        return null;
    }
}
