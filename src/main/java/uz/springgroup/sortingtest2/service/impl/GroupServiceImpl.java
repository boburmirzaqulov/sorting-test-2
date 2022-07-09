package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import uz.springgroup.sortingtest2.dto.FacultyDto;
import uz.springgroup.sortingtest2.dto.GroupDto;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.ValidatorDto;
import uz.springgroup.sortingtest2.entity.*;
import uz.springgroup.sortingtest2.exception.DatabaseException;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.mapper.GroupMapper;
import uz.springgroup.sortingtest2.repository.GroupRepository;
import uz.springgroup.sortingtest2.service.GroupService;
import uz.springgroup.sortingtest2.service.ValidationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final StudentServiceImpl studentService;
    private final JournalServiceImpl journalService;
    private final GroupMapper groupMapper;


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
        /**
         * V A L I D A T I O N
         */
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.idValid(id, errors);
        if (!errors.isEmpty()) {
            return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        }

        try {
            Optional<Group> groupOptional = groupRepository.findByIdAndIsActiveTrue(id);
            if (groupOptional.isPresent()) {
                List<Group> groups = new ArrayList<>();
                groups.add(groupOptional.get());
                setActive(false, groups);
                return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, id);
            } else {
                return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }

    }

    @Override
    public ResponseDto<List<GroupSt>> getStudents(Integer id) {
        if (!groupRepository.existsByIdAndIsActiveTrue(id)){
            return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
        }
        List<GroupSt> groupSts = groupRepository.getInfoStudents(id);
        return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, groupSts);
    }

    @Override
    public void setActiveAll(boolean b, List<Integer> facultyIds) {
        if (!facultyIds.isEmpty()) {
            try {
                List<Group> groups = groupRepository.findAllByFacultyIdIn(facultyIds);
                setActive(b, groups);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DatabaseException(e.getMessage(), e);
            }
        }
    }

    @Override
    public void setActiveOne(boolean b, Integer facultyId) {
        if (facultyId != null) {
            try {
                List<Group> groups = groupRepository.findAllByFacultyId(facultyId);
                setActive(b, groups);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DatabaseException(e.getMessage(), e);
            }
        }
    }

    @Override
    public ResponseDto<List<Group>> saveAllWithFacultyId(List<Group> groups, Integer facultyId) {
        try {
            for (Group group : groups) {
                group.setId(null);
                group.setFaculty(new Faculty(facultyId));
            }
            groupRepository.saveAll(groups);
            for (Group group : groups) {
                if (group.getStudents() != null) {
                    ResponseDto<List<Student>> responseDto = studentService.saveAllWithGroupId(
                            group.getStudents(),
                            group.getId()
                    );
                    if (responseDto.isSuccess()) {
                        group.setStudents(responseDto.getData());
                    }
                }
                if (group.getJournal() != null) {
                    ResponseDto<Journal> responseDto = journalService.saveWithGroupId(
                            group.getJournal(),
                            group.getId()
                    );
                    if (responseDto.isSuccess()){
                        group.setJournal(responseDto.getData());
                    }
                }
                group.setFaculty(null);
            }
            return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, groups);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseDto<>(false, AppCode.DATABASE_ERROR, AppMessages.DATABASE_ERROR, null);
        }
    }

    private void setActive(boolean b, List<Group> groups) {
        if (!groups.isEmpty()) {
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
}
