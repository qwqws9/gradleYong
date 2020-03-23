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
@Table(name = "CTG")
@Getter
@Setter
@ToString
public class CtgEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ctgSeq;

    @Column
    private Long ctgMstSeq;

    @Column
    private String dispNo;

    @Column
    private String dispYn;

    @Column
    private String ctgName;
}