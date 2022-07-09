package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.entity.Group;
import uz.springgroup.sortingtest2.entity.Journal;
import uz.springgroup.sortingtest2.entity.Subject;
import uz.springgroup.sortingtest2.exception.DatabaseException;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.repository.GroupRepository;
import uz.springgroup.sortingtest2.repository.JournalRepository;
import uz.springgroup.sortingtest2.service.JournalService;

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
        try {
            boolean b = groupRepository.existsByIdAndIsActiveTrue(groupId);
            if (!b) return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
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
