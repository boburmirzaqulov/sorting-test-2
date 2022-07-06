package uz.springgroup.sortingtest2.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "journal")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Journal {
    @Id
    @GeneratedValue(generator = "journal_id_seq")
    @SequenceGenerator(name = "journal_id_seq", sequenceName = "journal_id_seq", allocationSize = 1)
    private Integer id;

    private String name;

    @OneToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToMany
    @JoinTable(
            name = "journal_subject",
            joinColumns = @JoinColumn(name = "journal_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects;
}
