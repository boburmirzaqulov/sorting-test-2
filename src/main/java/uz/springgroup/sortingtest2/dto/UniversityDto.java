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
public class UniversityDto {
    private Integer id;
    @NotBlank(message = AppMessages.EMPTY_FIELD)
    private String name;
    @NotBlank(message = AppMessages.EMPTY_FIELD)
    private String address;
    @NotNull(message = AppMessages.EMPTY_FIELD)
    private Date openYear;
    private List<FacultyDto> faculties;
}
