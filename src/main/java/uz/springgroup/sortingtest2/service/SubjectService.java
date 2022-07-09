package uz.springgroup.sortingtest2.service;

import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.entity.Subject;

import java.util.List;

public interface SubjectService {

    ResponseDto<List<Subject>> saveAllWithJournalId(List<Subject> subjects, Integer journalId);
}
