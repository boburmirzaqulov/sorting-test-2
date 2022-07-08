package uz.springgroup.sortingtest2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StudentInfo {
    private Integer id;
    private String name;
    private String groupName;
    private String facultyName;
    private String universityName;
    private String journalName;
    private Map<String, Double> subjects;
}
