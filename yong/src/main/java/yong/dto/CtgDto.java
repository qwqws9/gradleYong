package yong.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CtgDto extends BaseDto {

    private Long ctgSeq;
    private Long ctgMstSeq;
    private String dispNo;
    private String dispYn;
    private String ctgName;
    private String ctgPath;
    
    private int ctgCount;
    
    
}
