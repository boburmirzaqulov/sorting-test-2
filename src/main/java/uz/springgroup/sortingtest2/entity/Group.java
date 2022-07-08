package uz.springgroup.sortingtest2.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "groups")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Group {
    @Id
    @GeneratedValue(generator = "group_id_seq")
    @SequenceGenerator(name = "group_id_seq", sequenceName = "group_id_seq", allocationSize = 1)
    private Integer id;

    private String name;

    private Date year;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Faculty faculty;

    @OneToMany(mappedBy = "group")
    private List<Student> students;

    @OneToOne(mappedBy = "group")
    private Journal journal;

    @ManyToMany
    @JoinTable(
            name = "group_subject",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Subject> subjects;

}
