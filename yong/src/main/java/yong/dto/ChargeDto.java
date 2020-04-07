package yong.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChargeDto {
    
    private String chargeId;
    private String stat;
    private String chargeType;
    private String lat;
    private String lng;
    private String AddrDoro;
    private String useTime;
    private String statNm;
    
    /** 주소 파라미터 Dto*/
    private String totalCount;
    private String addr;
    private String chargeTp;
    private String cpId;
    private String cpNm;
    private String cpStat;
    private String cpTp;
    private String csNm;
    //private String lat;
    private String longi;
    private String statUpdateTime;
    
    
    

}
