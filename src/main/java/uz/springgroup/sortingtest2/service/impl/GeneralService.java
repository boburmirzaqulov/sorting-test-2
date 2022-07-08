package uz.springgroup.sortingtest2.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.ValidatorDto;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.service.ValidationService;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeneralService {

    public static ResponseDto<Integer> deleteGeneral(JpaRepository repository, Integer id){
        // V A L I D A T I O N
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.idValid(id, errors);
        if (!errors.isEmpty()) {
            new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        }

        boolean existsById = false;
        try {
            existsById = repository.existsById(id);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseDto<>(false, AppCode.DATABASE_ERROR, AppMessages.DATABASE_ERROR, null);
        }

        if (!existsById){
            new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
        }

//        try {
//            repository.deleteById(id);
//        } catch (Exception e){
//            e.printStackTrace();
//            return new ResponseDto<>(false, AppCode.DATABASE_ERROR, AppMessages.DATABASE_ERROR, null);
//        }
//        return new ResponseDto<>(true, AppCode.OK, AppMessages.DELETED, id);

        return null;
    }


    public static ResponseDto<?> updateGeneral(JpaRepository repository, Integer id){
        // V A L I D A T I O N
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.idValid(id, errors);
        if (!errors.isEmpty()) {
            new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        }


        if (!repository.existsById(id)){
            return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, id);
        }
        return null;
    }
}
