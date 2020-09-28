package yong.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import yong.dto.DuntokiDto;

@Mapper
public interface DuntokiMapper {

    public List<DuntokiDto> selectDuntokiList(DuntokiDto param);
}
