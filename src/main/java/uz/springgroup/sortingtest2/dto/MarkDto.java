package uz.springgroup.sortingtest2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkDto {
    private Integer id;
    private StudentDto student;
    private SubjectDto subject;
    private Integer mark;
    private Date date;
}
