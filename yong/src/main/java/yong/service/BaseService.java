package yong.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yong.dto.BoardDto;
import yong.entity.BoardEntity;
import yong.jpa.BoardJpa;
import yong.mapper.BoardMapper;

@Service
public class BaseService {

    @Autowired
    private BoardMapper boardMapper;
    
    @Autowired
    private BoardJpa boardJpa;
    
    public List<BoardDto> test() throws Exception {
        return boardMapper.selectBoardList();
    }
    
    public BoardEntity test1() throws Exception {
        
        return boardJpa.findById(Long.valueOf(2)).orElse(null);
    }
    
    public void test2() throws Exception {
        BoardEntity entity = new BoardEntity();
        
        entity.setBoardTitle("등록");
        entity.setBoardContent("내용");
        entity.setBoardRegDt("20200228161610");
        
        boardJpa.save(entity);
        
//        entity.setBoardTitle("롤백");
//        entity.setBoardContent("내용");
//        entity.setBoardRegDt("20200228161610");
//        
//        boardJpa.save(entity);
        
        //int a = 10 / 0;
    }
    
    public void test3() throws Exception {
        BoardEntity entity = boardJpa.findById(Long.valueOf(2)).orElse(null);
        entity.setBoardContent("수정");
        entity.setBoardTitle("제목수정");
    }
}
