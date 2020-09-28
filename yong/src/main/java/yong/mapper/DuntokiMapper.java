package yong.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import yong.dto.BoardDto;

@Mapper
public interface DuntokiMapper {

    public List<BoardDto> selectBoardList(BoardDto param);

    public int selectBoardListCount();
}
