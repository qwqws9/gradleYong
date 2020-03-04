package yong.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import yong.common.HashUtil;
import yong.common.Result;
import yong.dto.BoardDto;
import yong.dto.UserDto;
import yong.entity.BoardEntity;
import yong.entity.FileEntity;
import yong.entity.UserEntity;
import yong.exception.BadRequestException;
import yong.jpa.BoardJpa;
import yong.jpa.FilesJpa;
import yong.jpa.UserJpa;
import yong.mapper.BoardMapper;

@Service
@Slf4j
public class BoardService {

    @Autowired
    private BoardJpa boardJpa;
    
    @Autowired
    private FilesJpa filesJpa;
    
    @Autowired
    private BoardMapper boardMapper;
    
    public List<BoardDto> boardList() {
        
        List<BoardDto> list = this.boardMapper.selectBoardList();
        
        for(BoardDto board : list) {
            List<FileEntity> fileList = this.filesJpa.findByBoardSeqAndDelYnOrderByFileSeq(board.getBoardSeq(), "N");
            if (fileList != null && fileList.size() != 0) {
                board.setFilePath(fileList.get(0).getFilePath());
            }
            board.setTextHtml(board.getTextHtml().replaceAll("&nbsp;", ""));
        }
        
        log.debug(" board ==> {}", list);
        
        return list;
    }
    
    public BoardDto boardSelect(String boardSeq) {
        
        BoardEntity board = this.boardJpa.findById(Long.valueOf(boardSeq)).orElse(null);
        if (board == null) { throw new BadRequestException("존재하지 않는 게시물입니다."); }
        
        return board.toDto(BoardDto.class);
    }
    
    public void boardSave(BoardDto board) {
        if (StringUtils.isEmpty(board.getBoardTitle()) || StringUtils.isEmpty(board.getBoardContent())) {
            throw new BadRequestException("폼 데이터 확인 후 재등록");
        }
        
        log.debug("textHtml ====> {}",board.getTextHtml());
        
        
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        board.setBoardRegDt(format.format(System.currentTimeMillis()));
        BoardEntity entity = this.boardJpa.save(board.toEntity(BoardEntity.class));
        
        if(!StringUtils.isEmpty(board.getTempSeq())) {
            List<FileEntity> list = this.filesJpa.findByBoardSeq(Long.valueOf(board.getTempSeq()));
            if (list != null && list.size() != 0) {
                for (FileEntity f : list) {
                    f.setDelYn("N");
                    f.setBoardSeq(entity.getBoardSeq());
                }
            }
        }
    }
    
    public void boardUpdate(BoardDto board) {
        if (StringUtils.isEmpty(board.getBoardTitle()) || StringUtils.isEmpty(board.getBoardContent())) {
            throw new BadRequestException("폼 데이터 확인 후 재등록");
        }
        
        BoardEntity entity = this.boardJpa.findById(board.getBoardSeq()).orElse(null);
        if (entity == null) { throw new BadRequestException("존재하지 않는 게시물입니다."); }
        
        entity.setBoardTitle(board.getBoardTitle());
        entity.setBoardContent(board.getBoardContent());
        entity.setTextHtml(board.getTextHtml());
        
        // 기존에 있던 이미지 파일 정리
        List<FileEntity> oldFileList = this.filesJpa.findByBoardSeq(Long.valueOf(board.getTempSeq()));
        for (FileEntity f : oldFileList) {
            if (board.getBoardTitle().indexOf(f.getFilePath()) == -1) {
                f.setDelYn("Y");
            }
        }

        // 새로 추가된 이미지 파일
        if(!StringUtils.isEmpty(board.getTempSeq())) {
            List<FileEntity> list = this.filesJpa.findByBoardSeq(Long.valueOf(board.getTempSeq()));
            if (list != null && list.size() != 0) {
                for (FileEntity f : list) {
                    f.setDelYn("N");
                    f.setBoardSeq(entity.getBoardSeq());
                }
            }
        }
    }

    
    /**
     * 
     * 유저 조회
     *
     * @since 2020. 3. 2.
     * @author yong
     *
     * @param user
     * @return
     */
//    public UserDto getUser(Long userSeq) {
//        UserEntity entity = this.userJpa.findById(userSeq).orElse(null);
//        if (entity == null) { throw new BadRequestException("존재하지 않는 회원입니다."); }
//        
//        UserDto user = entity.toDto(UserDto.class);
//        user.setUserPwd("");
//        
//        return user;
//    }
}
