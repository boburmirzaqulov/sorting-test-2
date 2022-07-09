package uz.springgroup.sortingtest2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.springgroup.sortingtest2.helper.AppMessages;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacultyDto {
    private Integer id;
    @NotBlank(message = AppMessages.EMPTY_FIELD)
    private String name;
    @NotNull(message = AppMessages.EMPTY_FIELD)
    private UniversityDto university;
    private List<GroupDto> groups;

    public FacultyDto(Integer id){
        this.id = id;
    }
}
