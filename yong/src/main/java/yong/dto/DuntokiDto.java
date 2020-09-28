package yong.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
//@ApiModel(value="BoardDto : 게시글 내용", description = "게시글 내용")
public class DuntokiDto extends BaseDto {

    public DuntokiDto() {
        // TODO Auto-generated constructor stub
    }
    public DuntokiDto(String mainId) {
        this.mainId = mainId;
    }
    private Long seqno;

    private String mainId;

    private String subId;

    private String serverName;

    private String dummy1;
    private String dummy2;
    private String dummy3;
    private String dummy4;
}
