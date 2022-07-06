package uz.springgroup.sortingtest2.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "faculty")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Faculty {
    @Id
    @GeneratedValue(generator = "faculty_id_seq")
    @SequenceGenerator(name = "faculty_id_seq", sequenceName = "faculty_id_seq", allocationSize = 1)
    private Integer id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "university_id")
    private University university;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "faculty")
    private List<Group> groups;
}
