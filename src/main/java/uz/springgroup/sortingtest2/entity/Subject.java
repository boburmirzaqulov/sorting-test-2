package uz.springgroup.sortingtest2.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subject")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Subject {
    @Id
    @GeneratedValue(generator = "subject_id_seq")
    @SequenceGenerator(name = "subject_id_seq", sequenceName = "subject_id_seq", allocationSize = 1)
    private Integer id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "group_subject",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<Group> groups;

    @ManyToMany
    @JoinTable(
            name = "journal_subject",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "journal_id"))
    private List<Journal> journals;

    @OneToMany(mappedBy = "subject")
    private List<Mark> markList;
}
