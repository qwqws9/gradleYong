package yong.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CtgMstDto extends BaseDto {

    private Long ctgMstSeq;
    private String dispNo;
    private String dispYn;
    private String ctgMstName;
}
