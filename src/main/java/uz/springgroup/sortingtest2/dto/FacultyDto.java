package uz.springgroup.sortingtest2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacultyDto {
    private Integer id;
    private String name;
    private UniversityDto university;
    private List<GroupDto> groups;
}
