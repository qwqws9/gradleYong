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
@Table(name = "DUNTOKI")
@Getter
@Setter
@ToString
public class DuntokiEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long seqno;

    @Column
    private String mainId;

    @Column
    private String subId;

    @Column
    private String serverName;

    @Column
    private String dummy1;
    @Column
    private String dummy2;
    @Column
    private String dummy3;
    @Column
    private String dummy4;

}
