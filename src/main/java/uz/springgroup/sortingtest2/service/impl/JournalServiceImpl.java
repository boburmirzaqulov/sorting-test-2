package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.ValidatorDto;
import uz.springgroup.sortingtest2.entity.Group;
import uz.springgroup.sortingtest2.entity.Journal;
import uz.springgroup.sortingtest2.entity.Subject;
import uz.springgroup.sortingtest2.exception.DatabaseException;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.repository.GroupRepository;
import uz.springgroup.sortingtest2.repository.JournalRepository;
import uz.springgroup.sortingtest2.service.JournalService;
import uz.springgroup.sortingtest2.service.ValidationService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalServiceImpl implements JournalService {
    private final JournalRepository journalRepository;
    private final SubjectServiceImpl subjectService;
    private final GroupRepository groupRepository;

    @Override
    public void setActiveAll(boolean b, List<Integer> groupIds) {
        if (!groupIds.isEmpty()) {
            try {
                List<Journal> journals = journalRepository.findAllByGroupIdIn(groupIds);
                for (Journal journal : journals) {
                    journal.setActive(b);
                }
                journalRepository.saveAll(journals);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DatabaseException(e.getMessage(), e);
            }
        }
    }

    @Override
    public ResponseDto<Journal> saveWithGroupId(Journal journal, Integer groupId) {
        /**
         * V A L I D A T I O N
         */
        List<ValidatorDto> errors = new ArrayList<>();
        errors.addAll(ValidationService.validationJournal(journal));
        if (!errors.isEmpty()) return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        try {
            if (!groupRepository.existsByIdAndIsActiveTrue(groupId)) return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseDto<>(false, AppCode.DATABASE_ERROR, AppMessages.DATABASE_ERROR, null);
        }

        /**
         * S A V I N G
         */
        try {
            journal.setId(null);
            journal.setGroup(new Group(groupId));
            journalRepository.save(journal);
            if (journal.getSubjects() != null) {
                ResponseDto<List<Subject>> responseDto = subjectService.saveAllWithJournalId(
                        journal.getSubjects(),
                        journal.getId()
                );
                if (responseDto.isSuccess()){
                    journal.setSubjects(responseDto.getData());
                } else {
                    return new ResponseDto<>(responseDto.isSuccess(), responseDto.getCode(), responseDto.getMessage(), null, responseDto.getErrors());
                }
            }
            journal.setGroup(null);
            return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, journal);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseDto<>(false, AppCode.DATABASE_ERROR, AppMessages.DATABASE_ERROR, null);
        }
    }
}
