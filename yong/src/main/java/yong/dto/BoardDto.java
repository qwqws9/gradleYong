package yong.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
//@ApiModel(value="BoardDto : 게시글 내용", description = "게시글 내용")
public class BoardDto extends BaseDto {

//    @ApiModelProperty(value = "게시글 번호")
    private Long boardSeq;
//    
//    @ApiModelProperty(value = "게시글 제목")
    private String boardTitle;
//    
//    @ApiModelProperty(value = "게시글 내용")
    private String boardContent;
//    
//    @ApiModelProperty(value = "게시글 작성시간")
    private String boardRegDt;
//    
//    @ApiModelProperty(value = "게시글 임시파일번호")
    private String tempSeq;
//    
//    @ApiModelProperty(value = "게시글 파일경로")
    private String filePath;
//    
//    @ApiModelProperty(value = "게시글 스마트에디터")
    private String textHtml;
//    
//    @ApiModelProperty(value = "카테고리 마스터 번호")
    private Long ctgMstSeq;
//    
//    @ApiModelProperty(value = "카테고리 번호 ")
    private Long ctgSeq;
//    
//    @ApiModelProperty(value = "카테고리 마스터 이름 ")
    private String ctgMstName;
//    
//    @ApiModelProperty(value = "카테고리 이름 ")
    private String ctgName;
//    
//    @ApiModelProperty(value = "무한스크롤 변수")
    private int startNum;
//    
//    @ApiModelProperty(value = "무한스크롤 변수")
    private int pageCount;
//    
//    @ApiModelProperty(value = "검색어")
    private String searchKeyword;
}
