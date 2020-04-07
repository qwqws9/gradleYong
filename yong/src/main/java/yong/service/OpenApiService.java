package yong.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import yong.dto.ChargeDto;
import yong.dto.MuseumDto;
import yong.exception.BadRequestException;

@Service
@Slf4j
public class OpenApiService {

    private static final String serviceKey = "EDN60Wz8jkdcTTGJ34WRQ69%2Ff8XfEh56%2BJJjpq26BZm1WERBZOTwLurZUp2slBtc9q28liz7cWcItx0KPRi7Ag%3D%3D";

//    public static void main(String[] args) {
        
//    }
    public List<ChargeDto> getChargeLocation(String addr) {
        List<ChargeDto> list = new ArrayList<>();
    
        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("http://openapi.kepco.co.kr/service/EvInfoServiceV2/getEvSearchList?ServiceKey=");
            urlBuilder.append(serviceKey);
            urlBuilder.append("&pageNo=1&");
            urlBuilder.append("numOfRows=3000&addr=");
            urlBuilder.append(addr);
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            JSONObject xml = XML.toJSONObject(sb.toString());
            xml = (JSONObject) xml.get("response");
            xml = (JSONObject) xml.get("body");
            String totalCount = String.valueOf(xml.get("totalCount"));
            xml = (JSONObject) xml.get("items");
            JSONArray arr = (JSONArray) xml.get("item");

            for (int i = 0; i < arr.length(); i++) {
                ChargeDto charge = new ChargeDto();
                JSONObject obj = (JSONObject) arr.get(i);
                charge.setAddr((String)obj.get("addr"));
//                charge.setChargeTp(String.valueOf(obj.get("chargeTp")));
                switch (String.valueOf(obj.get("chargeTp"))) {
                    case "2":
                        charge.setChargeTp("급속");
                        break;

                    default :
                        charge.setChargeTp("완속");
                        break;
                }
                charge.setCpId(String.valueOf(obj.get("cpId")));
                charge.setCpNm((String) obj.get("cpNm"));
                switch (String.valueOf(obj.get("cpStat"))) {
                    case "1":
                        charge.setCpStat("충전가능");
                        break;
                    case "2":
                        charge.setCpStat("충전중");
                        break;
                    case "3":
                        charge.setCpStat("고장/점검");
                        break;
                    case "4":
                        charge.setCpStat("통신장애");
                        break;
                    case "5":
                        charge.setCpStat("통신 미연결");
                        break;
                }
                if (obj.has("cpTp")) {
                    switch (String.valueOf(obj.get("cpTp"))) {
                        case "1":
                            charge.setCpTp("B타입(5핀)");
                            break;
                        case "2":
                            charge.setCpTp("C타입(5핀)");
                            break;
                        case "3":
                            charge.setCpTp("BC타입(5핀)");
                            break;
                        case "4":
                            charge.setCpTp("BC타입(7핀)");
                            break;
                        case "5":
                            charge.setCpTp("DC차데모");
                            break;
                        case "6":
                            charge.setCpTp("AC3상 ");
                            break;
                        case "7":
                            charge.setCpTp("DC콤보");
                            break;
                        case "8":
                            charge.setCpTp("DC차데모+DC콤보");
                            break;
                        default :
                            charge.setCpTp("DC차데모+DC콤보");
                    }
                }
                charge.setCsNm((String) obj.get("csNm"));
                charge.setLat(String.valueOf(obj.get("lat")));
                charge.setLng(String.valueOf(obj.get("longi")));
                charge.setStatUpdateTime((String)obj.get("statUpdateDatetime"));
                charge.setTotalCount(totalCount);
                
                System.out.println(charge.toString());
                list.add(charge);
            }
        } catch (Exception e) {
            log.error("openApi error");
        }

        return list;
    }
    
    
    public List<ChargeDto> getChargeLocation() {
        List<ChargeDto> list = new ArrayList<>();

        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("http://open.ev.or.kr:8080/openapi/services/rest/EvChargerService?ServiceKey=");
            urlBuilder.append(serviceKey);
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            JSONObject xml = XML.toJSONObject(sb.toString());
            xml = (JSONObject) xml.get("response");
            xml = (JSONObject) xml.get("body");
            xml = (JSONObject) xml.get("items");
            JSONArray arr = (JSONArray) xml.get("item");

            for (int i = 0; i < arr.length(); i++) {
                ChargeDto charge = new ChargeDto();
                JSONObject obj = (JSONObject) arr.get(i);
                charge.setChargeId(String.valueOf(obj.get("chgerId")));
                charge.setStat(String.valueOf(obj.get("stat")));
                charge.setChargeType(String.valueOf(obj.get("chgerType")));
                charge.setLat(String.valueOf(obj.get("lat")));
                charge.setLng(String.valueOf(obj.get("lng")));
                charge.setAddrDoro((String) obj.get("addrDoro"));
                charge.setUseTime(obj.has("useTime") ? (String) obj.get("useTime") : null);
                charge.setStatNm((String) obj.get("statNm"));
                list.add(charge);
            }
        } catch (Exception e) {
            log.error("openApi error");
        }

        return list;
    }
    
    

    public List<MuseumDto> getMuseumList(String pageNo, String rows) {
        List<MuseumDto> list = null;

        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(rows)) {
            rows = "15";
        }

        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("http://www.emuseum.go.kr/openapi/relic/list?serviceKey=");
            urlBuilder.append(serviceKey);
            urlBuilder.append("&pageNo=");
            urlBuilder.append(pageNo);
            urlBuilder.append("&numOfRows=");
            urlBuilder.append(rows);
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            JSONObject xml = XML.toJSONObject(sb.toString());

            JSONArray arr = (((JSONArray) ((JSONObject) ((JSONObject) xml.get("result")).get("list")).get("data")));

            list = new ArrayList<MuseumDto>();
            for (int i = 0; i < arr.length(); i++) {
                JSONArray a = (JSONArray) ((JSONObject) arr.get(i)).get("item");
                MuseumDto museum = new MuseumDto();
                for (int j = 0; j < a.length(); j++) {
                    JSONObject obj = (JSONObject) a.get(j);
                    Iterator<String> it = obj.keys();

                    while (it.hasNext()) {
                        String key = (String) obj.get(it.next());
                        switch (key) {
                            case "id":
                                museum.setId(obj.getString("value"));
                                break;
                            case "imgUri":
                                museum.setImgUri(obj.getString("value"));
                                break;
                            case "museumCode1":
                                museum.setMuseumCode(obj.getString("value"));
                                break;
                            case "museumName2":
                                museum.setMuseumName(obj.getString("value"));
                                break;
                            case "name":
                                museum.setName(obj.getString("value"));
                                break;
                        }
                    }
                }
                list.add(museum);
            }
        } catch (Exception e) {
            throw new BadRequestException("잘못된 요청..");
        }

        return list;
    }
}
