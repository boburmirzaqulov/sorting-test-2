package uz.springgroup.sortingtest2.service;

import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.entity.Journal;

import java.util.List;

public interface JournalService {
    void setActiveAll(boolean b, List<Integer> groupIds);

    ResponseDto<Journal> saveWithGroupId(Journal journal, Integer groupId);
}
