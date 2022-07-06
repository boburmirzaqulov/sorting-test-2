package uz.springgroup.sortingtest2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.springgroup.sortingtest2.helper.AppMessages;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    private Integer id;
    @NotBlank(message = AppMessages.EMPTY_FIELD)
    private String name;
    @NotNull(message = AppMessages.EMPTY_FIELD)
    private Date year;
    @NotNull(message = AppMessages.EMPTY_FIELD)
    private FacultyDto faculty;
    private List<StudentDto> students;
    private JournalDto journal;
    private List<SubjectDto> subjects;

}
