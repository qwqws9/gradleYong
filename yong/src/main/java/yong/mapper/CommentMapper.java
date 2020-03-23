package yong.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import yong.dto.CommentDto;

@Mapper
public interface CommentMapper {

    public int selectStepMaxCount(CommentDto comment);
    
    public List<CommentDto> selectCommentList(CommentDto comment);
    
    public List<CommentDto> selectCommentChildList(CommentDto comment);
}
