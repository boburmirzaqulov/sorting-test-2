package uz.springgroup.sortingtest2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.springgroup.sortingtest2.helper.AppMessages;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkDto {
    private Integer id;
    @NotNull(message = AppMessages.EMPTY_FIELD)
    private StudentDto student;
    @NotNull(message = AppMessages.EMPTY_FIELD)
    private SubjectDto subject;
    @NotNull(message = AppMessages.EMPTY_FIELD)
    private Integer mark;
    private Date date;
}
