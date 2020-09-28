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
import yong.jpa.DuntokiJpa;
import yong.jpa.FilesJpa;
import yong.jpa.UserJpa;
import yong.mapper.BoardMapper;

@Service
@Slf4j
public class DuntokiService {

    @Autowired
    private DuntokiJpa duntokiJpa;
    
    
    
    
}
