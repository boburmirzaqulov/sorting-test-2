package uz.springgroup.sortingtest2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDto {
    private Integer id;
    private String name;
    private List<GroupDto> groups;
    private List<JournalDto> journals;
    private List<MarkDto> markList;
}
