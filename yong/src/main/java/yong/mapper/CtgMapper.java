package yong.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import yong.dto.CtgDto;
import yong.dto.CtgMstDto;

@Mapper
public interface CtgMapper {

    public List<CtgMstDto> selectCtgMstList(String dispYn);
    
    public List<CtgDto> selectCtgList(CtgMstDto ctgMst);
}
