package yong.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import yong.common.DateFormat;
import yong.common.HashUtil;
import yong.common.Result;
import yong.dto.BoardDto;
import yong.dto.DuntokiDto;
import yong.dto.UserDto;
import yong.entity.BoardEntity;
import yong.entity.DuntokiEntity;
import yong.entity.FileEntity;
import yong.entity.UserEntity;
import yong.exception.BadRequestException;
import yong.jpa.BoardJpa;
import yong.jpa.DuntokiJpa;
import yong.jpa.FilesJpa;
import yong.jpa.UserJpa;
import yong.mapper.BoardMapper;

@Service
@Slf4j
public class DuntokiService {

    @Autowired
    private DuntokiJpa duntokiJpa;
    
    public void duntokiSave(DuntokiDto duntokiDto) {
        this.duntokiJpa.save(duntokiDto.toEntity(DuntokiEntity.class));
    }
    
    public List<DuntokiDto> duntokiList(DuntokiDto param) {

        List<DuntokiEntity> entity = this.duntokiJpa.findByMainId(param.getMainId());
        DuntokiDto dto = new DuntokiDto();
        List<DuntokiDto> list = new ArrayList<DuntokiDto>();
        
        if (entity.isEmpty()) {
            return null;
        } else {
            for (DuntokiEntity e : entity) {
                dto = new DuntokiDto();
                dto.setServerName(e.getServerName());
                dto.setSubId(e.getSubId());
                dto.setSeqno(e.getSeqno());
                dto.setMainId(e.getMainId());
                list.add(dto);
            }

            return list;
        }
    }
    
    public DuntokiDto duntokiDelete(DuntokiDto param) {

        List<DuntokiEntity> entity = this.duntokiJpa.findByMainIdAndSeqno(param.getMainId(), param.getSeqno());
        DuntokiDto dto = new DuntokiDto();
        
        if (entity.isEmpty()) {
            return null;
        } else {
            for (DuntokiEntity e : entity) {
                dto.setSubId(e.getSubId());
                this.duntokiJpa.deleteById(e.getSeqno());
            }
            
            return dto;
        }
    }
}
