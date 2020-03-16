package yong.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import yong.dto.BoardDto;

@Mapper
public interface BoardMapper {

    public List<BoardDto> selectBoardList(BoardDto param);

    public int selectBoardListCount();
}
