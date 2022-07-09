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

    /**
     * V A L I D A T I O N    F A C U L T Y
     */
    public static List<ValidatorDto> validationFacultyDto(FacultyDto facultyDto) {
        return validationFaculty(FacultyMapper.INSTANCE.toEntity(facultyDto));
    }

    public static List<ValidatorDto> validationFaculty(Faculty faculty) {
        List<ValidatorDto> errors = new ArrayList<>();
        if (faculty.getName() == null) errors.add(new ValidatorDto("Faculty name", AppMessages.EMPTY_FIELD));
        if (faculty.getUniversity() == null) errors.add(new ValidatorDto("Faculty university", AppMessages.EMPTY_FIELD));
        return errors;
    }

    public static List<ValidatorDto> validationFacultyDto(List<FacultyDto> facultyDtoList) {
        List<ValidatorDto> errors = new ArrayList<>();
        for (FacultyDto facultyDto : facultyDtoList) {
            errors.addAll(validationFacultyDto(facultyDto));
        }
        return errors;
    }

    public static List<ValidatorDto> validationFaculty(List<Faculty> faculties) {
        List<ValidatorDto> errors = new ArrayList<>();
        for (Faculty faculty : faculties) {
            errors.addAll(validationFaculty(faculty));
        }
        return errors;
    }

    /**
     * V A L I D A T I O N    G R O U P
     */
    public static List<ValidatorDto> validationGroupDto(GroupDto groupDto) {
        return validationGroup(GroupMapper.INSTANCE.toEntity(groupDto));
    }

    public static List<ValidatorDto> validationGroupDto(List<GroupDto> groupDtoList) {
        List<ValidatorDto> errors = new ArrayList<>();
        for (GroupDto groupDto : groupDtoList) {
            errors.addAll(validationGroupDto(groupDto));
        }
        return errors;
    }

    public static List<ValidatorDto> validationGroup(Group group) {
        List<ValidatorDto> errors = new ArrayList<>();
        if (group.getName() == null) errors.add(new ValidatorDto("Group name", AppMessages.EMPTY_FIELD));
        if (group.getYear() == null) errors.add(new ValidatorDto("Group year", AppMessages.EMPTY_FIELD));
        if (group.getFaculty() == null) errors.add(new ValidatorDto("Group faculty", AppMessages.EMPTY_FIELD));
        return errors;
    }

    public static List<ValidatorDto> validationGroup(List<Group> groups) {
        List<ValidatorDto> errors = new ArrayList<>();
        for (Group group : groups) {
            errors.addAll(validationGroup(group));
        }
        return errors;
    }

    /**
     * V A L I D A T I O N    S T U D E N T
     */
    public static List<ValidatorDto> validationStudentDto(StudentDto studentDto) {
        return validationStudent(StudentMapper.INSTANCE.toEntity(studentDto));
    }

    public static List<ValidatorDto> validationStudentDto(List<StudentDto> studentDtoList) {
        List<ValidatorDto> errors = new ArrayList<>();
        for (StudentDto studentDto : studentDtoList) {
            errors.addAll(validationStudentDto(studentDto));
        }
        return errors;
    }

    public static List<ValidatorDto> validationStudent(Student student) {
        List<ValidatorDto> errors = new ArrayList<>();
        if (student.getName() == null) errors.add(new ValidatorDto("Student name", AppMessages.EMPTY_FIELD));
        if (student.getGroup() == null) errors.add(new ValidatorDto("Student group", AppMessages.EMPTY_FIELD));
        return errors;
    }

    public static List<ValidatorDto> validationStudent(List<Student> students) {
        List<ValidatorDto> errors = new ArrayList<>();
        for (Student student : students) {
            errors.addAll(validationStudent(student));
        }
        return errors;
    }

    /**
     * V A L I D A T I O N    M A R K
     */
    public static List<ValidatorDto> validationMarkDto(MarkDto markDto) {
        return validationMark(MarkMapper.INSTANCE.toEntity(markDto));
    }

    public static List<ValidatorDto> validationMarkDto(List<MarkDto> markDtoList) {
        List<ValidatorDto> errors = new ArrayList<>();
        for (MarkDto markDto : markDtoList) {
            errors.addAll(validationMarkDto(markDto));
        }
        return errors;
    }

    public static List<ValidatorDto> validationMark(Mark mark) {
        List<ValidatorDto> errors = new ArrayList<>();
        if (mark.getStudent() == null) errors.add(new ValidatorDto("Mark student", AppMessages.EMPTY_FIELD));
        if (mark.getSubject() == null) errors.add(new ValidatorDto("Mark subject", AppMessages.EMPTY_FIELD));
        if (mark.getMark() == null) errors.add(new ValidatorDto("Mark mark", AppMessages.EMPTY_FIELD));
        else if (mark.getMark() < 0) errors.add(new ValidatorDto("Mark mark", AppMessages.INCORRECT_TYPE));
        return errors;
    }

    public static List<ValidatorDto> validationMark(List<Mark> marks) {
        List<ValidatorDto> errors = new ArrayList<>();
        for (Mark mark : marks) {
            errors.addAll(validationMark(mark));
        }
        return errors;
    }

    /**
     * V A L I D A T I O N    J O U R N A L
     */
    public static List<ValidatorDto> validationJournalDto(JournalDto journalDto) {
        return validationJournal(JournalMapper.INSTANCE.toEntity(journalDto));
    }

    public static List<ValidatorDto> validationJournalDto(List<JournalDto> journalDtoList) {
        List<ValidatorDto> errors = new ArrayList<>();
        for (JournalDto journalDto : journalDtoList) {
            errors.addAll(validationJournalDto(journalDto));
        }
        return errors;
    }

    public static List<ValidatorDto> validationJournal(Journal journal) {
        List<ValidatorDto> errors = new ArrayList<>();
        if (journal.getName() == null) errors.add(new ValidatorDto("Journal name", AppMessages.EMPTY_FIELD));
        if (journal.getGroup() == null) errors.add(new ValidatorDto("Journal group", AppMessages.EMPTY_FIELD));
        return errors;
    }

    public static List<ValidatorDto> validationJournal(List<Journal> journals) {
        List<ValidatorDto> errors = new ArrayList<>();
        for (Journal journal : journals) {
            errors.addAll(validationJournal(journal));
        }
        return errors;
    }

    /**
     * V A L I D A T I O N    S U B J E C T
     */
    public static List<ValidatorDto> validationSubjectDto(SubjectDto subjectDto) {
        return validationSubject(SubjectMapper.INSTANCE.toEntity(subjectDto));
    }

    public static List<ValidatorDto> validationSubjectDto(List<SubjectDto> subjectDtoList) {
        List<ValidatorDto> errors = new ArrayList<>();
        for (SubjectDto subjectDto : subjectDtoList) {
            errors.addAll(validationSubjectDto(subjectDto));
        }
        return errors;
    }

    public static List<ValidatorDto> validationSubject(Subject subject) {
        List<ValidatorDto> errors = new ArrayList<>();
        if (subject.getName() == null) errors.add(new ValidatorDto("Subject name", AppMessages.EMPTY_FIELD));
        return errors;
    }

    public static List<ValidatorDto> validationSubject(List<Subject> subjects) {
        List<ValidatorDto> errors = new ArrayList<>();
        for (Subject subject : subjects) {
            errors.addAll(validationSubject(subject));
        }
        return errors;
    }
}
