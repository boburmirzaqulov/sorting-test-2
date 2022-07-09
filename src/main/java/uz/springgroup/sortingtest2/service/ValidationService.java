package uz.springgroup.sortingtest2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import uz.springgroup.sortingtest2.dto.*;
import uz.springgroup.sortingtest2.entity.*;
import uz.springgroup.sortingtest2.exception.DatabaseException;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.helper.StringHelper;
import uz.springgroup.sortingtest2.mapper.*;
import uz.springgroup.sortingtest2.repository.FacultyRepository;
import uz.springgroup.sortingtest2.repository.GroupRepository;
import uz.springgroup.sortingtest2.repository.UniversityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ValidationService {

    public static void idValid(Integer id, List<ValidatorDto> errors){
        if (id == null) {
            errors.add(new ValidatorDto("id", AppMessages.EMPTY_FIELD));
            return;
        }
        if (id < 0) errors.add(new ValidatorDto("id", AppMessages.INCORRECT_TYPE));
    }

    public static void getAllGeneral(MultiValueMap<String, String> params, List<ValidatorDto> errors){
        boolean isPage = StringHelper.isNumber(params.getFirst("page"));
        boolean isSize = StringHelper.isNumber(params.getFirst("size"));
        if (!isPage) errors.add(new ValidatorDto("page", AppMessages.NOT_FOUND));
        if (!isSize) errors.add(new ValidatorDto("size", AppMessages.NOT_FOUND));
    }

    public List<ValidatorDto> validationFaculty(FacultyDto facultyDto) {
        return validationFaculty(FacultyMapper.INSTANCE.toEntity(facultyDto));
    }

    public static List<ValidatorDto> validationFaculty(Faculty faculty) {
        List<ValidatorDto> errors = new ArrayList<>();
        if (faculty.getName() == null) errors.add(new ValidatorDto("Faculty name", AppMessages.EMPTY_FIELD));
        return errors;
    }

    public static List<ValidatorDto> validationGroup(GroupDto groupDto) {
        return validationGroup(GroupMapper.INSTANCE.toEntity(groupDto));
    }

    public static List<ValidatorDto> validationGroup(Group group) {
        List<ValidatorDto> errors = new ArrayList<>();
        if (group.getName() == null) errors.add(new ValidatorDto("Group name", AppMessages.EMPTY_FIELD));
        if (group.getYear() == null) errors.add(new ValidatorDto("Group year", AppMessages.EMPTY_FIELD));
        if (group.getFaculty() == null) errors.add(new ValidatorDto("Group faculty", AppMessages.EMPTY_FIELD));
        return errors;
    }

    public static List<ValidatorDto> validationStudent(StudentDto studentDto) {
        return validationStudent(StudentMapper.INSTANCE.toEntity(studentDto));
    }

    public static List<ValidatorDto> validationStudent(Student student) {
        List<ValidatorDto> errors = new ArrayList<>();
        if (student.getName() == null) errors.add(new ValidatorDto("Student name", AppMessages.EMPTY_FIELD));
        if (student.getGroup() == null) errors.add(new ValidatorDto("Student group", AppMessages.EMPTY_FIELD));
        return errors;
    }

    public static List<ValidatorDto> validationMark(MarkDto markDto) {
        return validationMark(MarkMapper.INSTANCE.toEntity(markDto));
    }

    public static List<ValidatorDto> validationMark(Mark mark) {
        List<ValidatorDto> errors = new ArrayList<>();
        if (mark.getStudent() == null) errors.add(new ValidatorDto("Mark student", AppMessages.EMPTY_FIELD));
        if (mark.getSubject() == null) errors.add(new ValidatorDto("Mark subject", AppMessages.EMPTY_FIELD));
        if (mark.getMark() == null) errors.add(new ValidatorDto("Mark mark", AppMessages.EMPTY_FIELD));
        else if (mark.getMark() < 0) errors.add(new ValidatorDto("Mark mark", AppMessages.INCORRECT_TYPE));
        return errors;
    }

    public static List<ValidatorDto> validationJournal(JournalDto journalDto) {
        return validationJournal(JournalMapper.INSTANCE.toEntity(journalDto));
    }

    public static List<ValidatorDto> validationJournal(Journal journal) {
        List<ValidatorDto> errors = new ArrayList<>();
        if (journal.getName() == null) errors.add(new ValidatorDto("Journal name", AppMessages.EMPTY_FIELD));
        if (journal.getGroup() == null) errors.add(new ValidatorDto("Journal group", AppMessages.EMPTY_FIELD));
        return errors;
    }

    public static List<ValidatorDto> validationSubject(SubjectDto subjectDto) {
        return validationSubject(SubjectMapper.INSTANCE.toEntity(subjectDto));
    }

    public static List<ValidatorDto> validationSubject(Subject subject) {
        List<ValidatorDto> errors = new ArrayList<>();
        if (subject.getName() == null) errors.add(new ValidatorDto("Subject name", AppMessages.EMPTY_FIELD));
        return errors;
    }

    public static void validationFacultyDtoForSave(FacultyDto facultyDto, List<ValidatorDto> errors, UniversityRepository universityRepository, GroupRepository groupRepository) {
        UniversityDto universityDto = facultyDto.getUniversity();
        if (universityDto != null){
            if (universityDto.getId() != null){
                boolean uR;
                try {
                    uR = universityRepository.existsByIdAndIsActiveTrue(facultyDto.getUniversity().getId());
                } catch (Exception e){
                    e.printStackTrace();
                    throw new DatabaseException(e.getMessage(), e);
                }
                if (!uR) errors.add(new ValidatorDto("University ID", AppMessages.NOT_FOUND));
            }
        }

        List<GroupDto> groups = facultyDto.getGroups();
        if (groups != null){
            if (!groups.isEmpty()){
                List<Integer> groupIds = groups.stream()
                        .map(GroupDto::getId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());
                List<Integer> groupIdsDB;
                try {
                    groupIdsDB = groupRepository.findAllByIdInAndIsActiveTrue(groupIds).stream()
                            .map(Group::getId)
                            .distinct()
                            .collect(Collectors.toList());
                } catch (Exception e){
                    e.printStackTrace();
                    throw new DatabaseException(e.getMessage(), e);
                }
                if (groupIds.size() != groupIdsDB.size()){
                    groupIds.removeAll(groupIdsDB);
                    errors.add(
                            new ValidatorDto(String.format("Group Ids %s ", groupIds.toArray()), AppMessages.NOT_FOUND)
                    );
                }
            }
        }
    }
}
