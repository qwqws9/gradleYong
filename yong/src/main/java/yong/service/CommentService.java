package yong.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import yong.common.Const;
import yong.common.DateFormat;
import yong.common.HashUtil;
import yong.common.Result;
import yong.dto.CommentDto;
import yong.entity.CommentEntity;
import yong.exception.BadRequestException;
import yong.jpa.CommentJpa;
import yong.mapper.CommentMapper;

@Service
@Slf4j
public class CommentService {
    
    @Autowired
    private CommentJpa commentJpa;
    
    @Autowired
    private CommentMapper commentMapper;

    public Result commentSave(CommentDto comment) {
        
        if (StringUtils.isEmpty(comment.getContent()) || StringUtils.isEmpty(comment.getWriter()) || StringUtils.isEmpty(comment.getPwd())) {
            return new Result(100, "폼 데이터 누락");
        }
        
        comment.setStep(0);
        comment.setDelYn(Const.N);
        comment.setRegDt(DateFormat.getRegDate());
        comment.setPwd(HashUtil.sha512(comment.getPwd(), comment.getPwd()));

        if (comment.getParentSeq() != null && comment.getParentSeq() != 0) {
            comment.setStep(this.commentMapper.selectStepMaxCount(comment));
        }
        
        CommentEntity entity = this.commentJpa.save(comment.toEntity(CommentEntity.class));

        if (comment.getParentSeq() == null || comment.getParentSeq() == 0) {
            entity.setParentSeq(entity.getCommentSeq());
        }
        
        return new Result();
    }
    
    public List<CommentDto> selectCommentList(CommentDto comment) {
        if (comment.getBoardSeq() == null || comment.getBoardSeq() == 0) {
            throw new BadRequestException("잘못된 접근");
        }

        return this.commentMapper.selectCommentList(comment);
    }
    
    public List<CommentDto> selectCommentChildList(CommentDto comment) {
        if (comment.getBoardSeq() == null || comment.getBoardSeq() == 0) {
            throw new BadRequestException("잘못된 접근");
        }
        
        return this.commentMapper.selectCommentChildList(comment);
    }
    
    
}
