package uz.springgroup.sortingtest2.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.ValidatorDto;
import uz.springgroup.sortingtest2.exception.DatabaseException;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.service.ValidationService;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeneralService {

    public static ResponseDto<Integer> deleteGeneral(JpaRepository repository, Integer id){




//        try {
//            repository.deleteById(id);
//        } catch (Exception e){
//            e.printStackTrace();
//            return new ResponseDto<>(false, AppCode.DATABASE_ERROR, AppMessages.DATABASE_ERROR, null);
//        }
//        return new ResponseDto<>(true, AppCode.OK, AppMessages.DELETED, id);

        return null;
    }
}
