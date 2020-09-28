package yong.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import yong.dto.DuntokiDto;
import yong.service.DuntokiService;

/**
 * 
 * 클래스명: <code>BoardController</code><br/><br/>
 *
 * 게시물 조회/수정/삭제 
 *
 * @since 2020. 3. 16.
 * @author yong
 *
 */
@Controller
@Slf4j
public class DuntokiController extends BaseController {

    @Autowired
    private DuntokiService duntokiService;

    @RequestMapping("/teset/aaa/{server}/{mainId}")
    @ResponseBody
    public String insert(@PathVariable String server, @PathVariable String mainId, @RequestParam String name) throws ParseException {
        System.out.println(server);
        System.out.println(mainId);
        System.out.println(name);
        
        DuntokiDto dto = new DuntokiDto();
        dto.setServerName(server);
        dto.setMainId(mainId);
        
        name = name.replaceAll("\\[", "");
        name = name.replaceAll("\\]", "");
        name = name.replaceAll("\"", "");
        
        String[] nameArr = name.split(",");
        
        List<String> list2 = new ArrayList<>();
        for (String temp : nameArr) {
            list2.add(temp);
        }
        
        
        List<DuntokiDto> list = this.duntokiService.duntokiList(new DuntokiDto(mainId));
        for (DuntokiDto dto2 : list) {
            boolean chk = Arrays.stream(nameArr).anyMatch(dto2.getSubId()::equals);
            if (chk) {
                list2.remove(dto2.getSubId());
            }
        }
        
        for (int i = 0; i < list2.size(); i++) {
            dto.setSubId(list2.get(i));
            this.duntokiService.duntokiSave(dto);
        }
        
        String returnMsg = "";
        for (int i = 0; i < list2.size(); i++) {
            returnMsg += list2.get(i);
            if (i != list2.size() - 1) {
                returnMsg += ",";
            }
        }
        
        if (nameArr.length != list2.size()) {
            return "중복된 데이터를 제외하고 " + returnMsg + " (" +list2.size() + ")건 추가했습니다.";
        }
        
        return "추가 완료 " + returnMsg;
    }
    
    @RequestMapping("/teset/select/{mainId}/{yn}")
    @ResponseBody
    public String select(@PathVariable String mainId, @PathVariable String yn) throws ParseException {
        StringBuilder sb = new StringBuilder();
        
        List<DuntokiDto> list = this.duntokiService.duntokiList(new DuntokiDto(mainId));
        if (list == null) {
            return mainId + "로 등록된 캐릭터가 존재하지 않습니다.";
        }
        
        boolean firstChk = true;
        for (DuntokiDto dto : list) {
            if (firstChk) {
                sb.append(dto.getMainId());
                sb.append(" - ");
                sb.append(dto.getServerName()); sb.append("*emrms*");
                sb.append("--------------------");
                firstChk = false;
            }
            sb.append(dto.getSeqno()); sb.append(" - ");
            sb.append(dto.getSubId()); sb.append("*emrms*");
        }
        
        if ("Y".equals(yn)) {
            sb.append("*emrms*");
            sb.append("삭제할 캐릭터명 앞에 숫자를"); sb.append("*emrms*");
            sb.append("/삭제 {모험단명} {숫자}"); sb.append("*emrms*");
            sb.append("형식으로 입력해주세요.");
        }
        
        return sb.toString();
    }
    
    
    @RequestMapping("/teset/delete/{mainId}/{seqno}")
    @ResponseBody
    public String delete(@PathVariable String mainId, @PathVariable String seqno) throws ParseException {
        DuntokiDto dto = new DuntokiDto();
        dto.setMainId(mainId);
        if (!NumberUtils.isNumber(seqno)) {
            return "숫자를 입력해주세요.";
        }
        dto.setSeqno(Long.valueOf(seqno));
        
        DuntokiDto returnDto = this.duntokiService.duntokiDelete(dto);
        
        if (returnDto == null) {
            return mainId + "로 등록된 캐릭터가 존재하지 않습니다.";
        }
        
        return returnDto.getSubId() + " 삭제완료";
    }
}
