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
import yong.dto.CtgMstDto;
import yong.dto.UserDto;
import yong.entity.BoardEntity;
import yong.entity.CtgEntity;
import yong.entity.CtgMstEntity;
import yong.entity.FileEntity;
import yong.entity.UserEntity;
import yong.exception.BadRequestException;
import yong.jpa.BoardJpa;
import yong.jpa.CtgJpa;
import yong.jpa.CtgMstJpa;
import yong.jpa.FilesJpa;
import yong.jpa.UserJpa;
import yong.mapper.BoardMapper;
import yong.mapper.CtgMapper;

@Service
@Slf4j
public class CtgService {

    @Autowired
    private BoardJpa boardJpa;
    
    @Autowired
    private FilesJpa filesJpa;
    
    @Autowired
    private BoardMapper boardMapper;
    
    @Autowired
    private CtgJpa ctgJpa;
    
    @Autowired
    private CtgMstJpa ctgMstJpa;
    
    @Autowired
    private CtgMapper ctgMapper;
    
    public List<CtgMstDto> ctgList(String ctgAll) {
        List<CtgMstDto> ctgMstList = this.ctgMapper.selectCtgMstList(ctgAll);
        
        if (ctgMstList != null && ctgMstList.size() != 0) {
            for (CtgMstDto c : ctgMstList) {
                if (StringUtils.isNotEmpty(ctgAll)) {
                    c.setCtgAll(ctgAll);
                }
                c.setCtgDtoList(this.ctgMapper.selectCtgList(c));
            }
        }
        
        return ctgMstList;
    }
    
    public Result ctgMstSave(CtgMstDto ctgMst) {
        
        if(!StringUtils.isEmpty(ctgMst.getLoc())) {
            switch (ctgMst.getLoc()) {
                case "update":
                    if (ctgMst.getCtgMstSeq() != null && ctgMst.getCtgMstSeq() != 0 && StringUtils.isEmpty(ctgMst.getCtgSeq())) {
                        CtgMstEntity mstEntity = this.ctgMstJpa.findById(ctgMst.getCtgMstSeq()).orElse(null);
                        if (mstEntity == null) { throw new BadRequestException("수정할 수 없는 대상입니다."); }
                        mstEntity.setCtgName(ctgMst.getCtgName());
                        mstEntity.setDispNo(ctgMst.getDispNo());
                        mstEntity.setDispYn(ctgMst.getDispYn());
                    } else {
                        CtgEntity ctgEntity = this.ctgJpa.findById(Long.valueOf(ctgMst.getCtgSeq())).orElse(null);
                        if (ctgEntity == null) { throw new BadRequestException("수정할 수 없는 대상입니다."); }
                        ctgEntity.setCtgName(ctgMst.getCtgName());
                        ctgEntity.setDispNo(ctgMst.getDispNo());
                        ctgEntity.setDispYn(ctgMst.getDispYn());
                    }
                    break;
                case "parent" :
                    log.debug("마스터 생성?");
                    this.ctgMstJpa.save(ctgMst.toEntity(CtgMstEntity.class));
                    break;
                case "child" :
                    this.ctgJpa.save(ctgMst.toEntity(CtgEntity.class));
                    break;
                default:
                    return new Result(100, "생성 정보가 없습니다.");
            }
        }
        
        return new Result();
    }
}
