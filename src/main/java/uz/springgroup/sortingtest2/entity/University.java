package uz.springgroup.sortingtest2.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "university")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class University {
    @Id
    @GeneratedValue(generator = "university_id_seq")
    @SequenceGenerator(name = "university_id_seq", sequenceName = "university_id_seq", allocationSize = 1)
    private Integer id;

    private String name;

    private String address;

    @Column(name = "open_year")
    private Date openYear;

    @OneToMany(mappedBy = "university")
    private List<Faculty> faculties;

    @Column(name = "is_active")
    private boolean isActive;
}
