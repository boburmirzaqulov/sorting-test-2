package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import uz.springgroup.sortingtest2.dto.FacultyDto;
import uz.springgroup.sortingtest2.dto.GroupDto;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.entity.Group;
import uz.springgroup.sortingtest2.entity.GroupSt;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.repository.GroupRepository;
import uz.springgroup.sortingtest2.service.GroupService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final StudentServiceImpl studentService;
    private final JournalServiceImpl journalService;
    private final SubjectServiceImpl subjectService;


    @Override
    public ResponseDto<GroupDto> save(GroupDto groupDto) {
        return null;
    }

    @Override
    public ResponseDto<?> getAll(MultiValueMap<String, String> params) {
        return null;
    }

    @Override
    public ResponseDto<FacultyDto> getById(Integer id) {
        return null;
    }

    @Override
    public ResponseDto<?> update(FacultyDto facultyDto) {
        return null;
    }

    @Override
    public ResponseDto<Integer> delete(Integer id) {
        return null;
    }

    @Override
    public ResponseDto<List<GroupSt>> getStudents(Integer id) {
        List<GroupSt> groupSts = groupRepository.getInfoStudents(id);
        return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, groupSts);
    }

    @Override
    public boolean setActiveAll(boolean b, List<Integer> facultyIds) {
        try {
            List<Group> groups = groupRepository.findAllByFacultyIdIn(facultyIds);
            List<Integer> groupIds = new ArrayList<>();
            for (Group group : groups) {
                group.setActive(b);
                groupIds.add(group.getId());
            }
            boolean student;
            student = studentService.setActiveAll(b, groupIds);
            if (student) {
                boolean journal = journalService.setActiveAll(b, groupIds);
                if (journal){
                    groupRepository.saveAll(groups);
                }
                return journal;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean setActiveOne(boolean b, Integer facultyId) {
        return false;
    }
}
