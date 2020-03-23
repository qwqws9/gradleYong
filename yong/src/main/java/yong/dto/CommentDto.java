package yong.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDto extends BaseDto {

    private Long commentSeq;
    private Long boardSeq;
    private String content;
    private String regDt;
    private String writer;
    private String pwd;
    private String delYn;
    private int step;
    private Long parentSeq;
}
