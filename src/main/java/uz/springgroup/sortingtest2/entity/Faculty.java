package uz.springgroup.sortingtest2.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne
    @JoinColumn(name = "university_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private University university;

    @OneToMany(mappedBy = "faculty")
    private List<Group> groups;

    @Column(name = "is_active")
    private boolean isActive;

    public Faculty(Integer id) {
        this.id = id;
    }
}
