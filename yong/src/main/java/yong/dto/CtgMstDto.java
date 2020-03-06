package yong.dto;

import java.util.List;

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
    private String ctgName;
    private String ctgSeq;
    private String loc;
    private List<CtgDto> ctgDtoList;
    private String ctgAll;
}
