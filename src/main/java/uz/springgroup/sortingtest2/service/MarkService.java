package uz.springgroup.sortingtest2.service;

import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.entity.Mark;

import java.util.List;

public interface MarkService {
    void setActiveAll(boolean b, List<Integer> studentIds);

    ResponseDto<List<Mark>> saveAllWithStudentId(List<Mark> markList, Integer id);
}
