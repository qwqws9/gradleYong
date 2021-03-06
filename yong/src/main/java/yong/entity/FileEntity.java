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
@Table(name = "FILES")
@Getter
@Setter
@ToString
public class FileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long fileSeq;

    @Column
    private int masterSeq;

    @Column
    private Long boardSeq;

    @Column
    private String originalFileName;

    @Column
    private String filePath;

    @Column
    private Long fileSize;

    @Column
    private String delYn;
}
