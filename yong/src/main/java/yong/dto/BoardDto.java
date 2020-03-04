package yong.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardDto extends BaseDto {

    private Long boardSeq;
    private String boardTitle;
    private String boardContent;
    private String boardRegDt;
    private String tempSeq;
    private String filePath;
    private String textHtml;
}
