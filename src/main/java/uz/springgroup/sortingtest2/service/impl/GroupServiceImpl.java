package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import uz.springgroup.sortingtest2.dto.FacultyDto;
import uz.springgroup.sortingtest2.dto.GroupDto;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.entity.Group;
import uz.springgroup.sortingtest2.entity.GroupSt;
import uz.springgroup.sortingtest2.exception.DatabaseException;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.repository.GroupRepository;
import uz.springgroup.sortingtest2.service.GroupService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        // W I T H   V A L I D A T I O N
        ResponseDto<Integer> res = GeneralService.deleteGeneral(groupRepository, id);

        if (res == null){
            try {
                Optional<Group> groupOptional = groupRepository.findById(id);
                if (groupOptional.isPresent()) {
                    List<Group> groups = new ArrayList<>();
                    groups.add(groupOptional.get());
                    setActive(false, groups);
                    return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, id);
                }
            } catch (Exception e){
                e.printStackTrace();
                throw new DatabaseException(e.getMessage(),e);
            }
        }
        return res;
    }

    @Override
    public ResponseDto<List<GroupSt>> getStudents(Integer id) {
        List<GroupSt> groupSts = groupRepository.getInfoStudents(id);
        return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, groupSts);
    }

    @Override
    public void setActiveAll(boolean b, List<Integer> facultyIds) {
        try {
            List<Group> groups = groupRepository.findAllByFacultyIdIn(facultyIds);
            setActive(b, groups);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public void setActiveOne(boolean b, Integer facultyId) {
        try {
            List<Group> groups = groupRepository.findAllByFacultyId(facultyId);
            setActive(b, groups);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    private void setActive(boolean b, List<Group> groups) {
        try {
            List<Integer> groupIds = new ArrayList<>();
            for (Group group : groups) {
                group.setActive(b);
                groupIds.add(group.getId());
            }
            studentService.setActiveAll(b, groupIds);
            journalService.setActiveAll(b, groupIds);
            groupRepository.saveAll(groups);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
