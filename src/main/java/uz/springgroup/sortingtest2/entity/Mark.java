package uz.springgroup.sortingtest2.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mark")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Mark {
    @Id
    @GeneratedValue(generator = "mark_id_seq")
    @SequenceGenerator(name = "mark_id_seq", sequenceName = "mark_id_seq", allocationSize = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private Integer mark;

    private Date date;
}
