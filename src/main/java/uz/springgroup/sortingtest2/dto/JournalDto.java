package uz.springgroup.sortingtest2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalDto {
    private Integer id;
    private String name;
    private GroupDto group;
    private List<SubjectDto> subjects;
}
