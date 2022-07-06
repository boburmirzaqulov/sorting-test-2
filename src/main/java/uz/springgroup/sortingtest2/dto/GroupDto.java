package uz.springgroup.sortingtest2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    private Integer id;
    private String name;
    private Date year;
    private FacultyDto faculty;
    private List<StudentDto> students;
    private JournalDto journal;
    private List<SubjectDto> subjects;

}
