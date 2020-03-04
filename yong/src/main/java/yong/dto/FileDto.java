package yong.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDto extends BaseDto {

    private Long fileSeq;
    private int masterSeq;
    private Long boardSeq;
    private String originalFileName;
    private String filePath;
    private Long fileSize;
    private String delYn;
}
