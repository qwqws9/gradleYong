package yong.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto extends BaseDto {

    private Long userSeq;
    private String userId;
    private String userPwd;
    private String userRole;
    private String userNick;
    private String userStatCd;
    private String regDt;
    
}
