package yong.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "COMMENTS")
@Getter
@Setter
@ToString
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentSeq;
    @Column
    private Long boardSeq;
    @Column
    private String content;
    @Column
    private String regDt;
    @Column
    private String writer;
    @Column
    private String pwd;
    @Column
    private String delYn;
    @Column
    private int step;
    @Column
    private Long parentSeq;
}
