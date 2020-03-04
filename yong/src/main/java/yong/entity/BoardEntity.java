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
@Table(name = "BOARD")
@Getter
@Setter
@ToString
public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long boardSeq;
    @Column
    private String boardTitle;
    @Column
    private String boardContent;
    @Column(updatable = false)
    private String boardRegDt;
    @Column
    private String textHtml;
}
