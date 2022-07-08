package uz.springgroup.sortingtest2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ValidatorDto {
    private String fieldName;
    private String error;
}
