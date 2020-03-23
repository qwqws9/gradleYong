package yong.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import yong.common.DateFormat;
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
    
    /**
     * 
     * 게시물 전체 리스트 조회
     *
     * @since 2020. 3. 16.
     * @author yong
     *
     * @param param
     * @return
     */
    public List<BoardDto> boardList(BoardDto param) {

        List<BoardDto> list = this.boardMapper.selectBoardList(param);

        for(BoardDto board : list) {
            List<FileEntity> fileList = this.filesJpa.findByBoardSeqAndDelYnOrderByFileSeq(board.getBoardSeq(), "N");
            if (fileList != null && fileList.size() != 0) {
                board.setFilePath(fileList.get(0).getFilePath());
            }

            // 게시물 텍스트 태그 제거 및 글자수 제한
            board.setTextHtml(board.getTextHtml().replaceAll("&nbsp;", ""));
            if (board.getTextHtml().length() > 300) {
                board.setTextHtml(board.getTextHtml().substring(0, 300)+"...");
            }
            
        }

        log.debug(" board ==> {}", list);

        return list;
    }

    /**
     * 
     * 게시물 조회
     *
     * @since 2020. 3. 16.
     * @author yong
     *
     * @param boardSeq
     * @return
     */
    public BoardDto boardSelect(String boardSeq) {

        BoardEntity board = this.boardJpa.findById(Long.valueOf(boardSeq)).orElse(null);
        if (board == null) { throw new BadRequestException("존재하지 않는 게시물입니다."); }

        return board.toDto(BoardDto.class);
    }

    /**
     * 
     * 게시물 등록
     *
     * @since 2020. 3. 16.
     * @author yong
     *
     * @param board
     */
    public void boardSave(BoardDto board) {
        if (StringUtils.isEmpty(board.getBoardTitle()) || StringUtils.isEmpty(board.getBoardContent())) {
            throw new BadRequestException("폼 데이터 확인 후 재등록");
        }

        log.debug("textHtml ====> {}",board.getTextHtml());

        board.setBoardRegDt(DateFormat.getRegDate());
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

    /**
     * 
     * 게시물 수정
     *
     * @since 2020. 3. 16.
     * @author yong
     *
     * @param board
     */
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
     * 게시물 개수 조회
     *
     * @since 2020. 3. 16.
     * @author yong
     *
     * @return
     */
    public int selectBoardListCount() {
        return this.boardMapper.selectBoardListCount();
    }
}
