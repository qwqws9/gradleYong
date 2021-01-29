package yong.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PuppeteerController extends BaseController {

	private static final String NEOPLE = "https://api.neople.co.kr/df/";
    private static final String API_KEY = "NZsA1lAqj64UpeGK1XQxEfUU3PZUWOmw";
    private static Map<String, String> SERVER;
    
    static {
        SERVER = new HashMap<String, String>();
        SERVER.put("카인", "cain");
        SERVER.put("디레지에", "diregie");
        SERVER.put("시로코", "siroco");
        SERVER.put("프레이", "prey");
        SERVER.put("카시야스", "casillas");
        SERVER.put("힐더", "hilder");
        SERVER.put("안톤", "anton");
        SERVER.put("바칼", "bakal");
    }
    
	@SuppressWarnings("unchecked")
	@RequestMapping("/puppeteer/{server}/{name}/{buf}")
    @ResponseBody
    public String puppeteer(@PathVariable String server, @PathVariable String name, @PathVariable String buf) throws UnsupportedEncodingException {
        String neopleServer = "";
        JSONObject resObj = new JSONObject();
        

		if (SERVER.containsKey(server)) {
            neopleServer = SERVER.get(server);
        } else {
        	resObj.put("status", "400");
        	resObj.put("msg", "서버명을 확인해주세요.");
            return resObj.toJSONString();
        }
		
		String neopleId = this.getCharacterId(neopleServer, name);
		
		if ("x".equals(neopleId)) {
			resObj.put("status", "400");
			resObj.put("msg", "서버와 캐릭터명을 확인해주세요.");
        	return resObj.toJSONString();
		}
		
		resObj.put("img", "https://img-api.neople.co.kr/df/servers/" + neopleServer +"/characters/"+ neopleId + "?zoom=1");
		resObj.put("img2", "https://img-api.neople.co.kr/df/servers/" + neopleServer +"/characters/"+ neopleId + "?zoom=3");
        
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL("http://104.196.235.254:9999/");
//            URL url = new URL("http://localhost:9999/");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "applitcation/json");
            conn.setRequestProperty("Content-type", "application/json");
            
            OutputStream out = conn.getOutputStream();
            JSONObject obj = new JSONObject();
            JSONObject data = new JSONObject();
            
            obj.put("nick", name);
            obj.put("characterid", neopleId);
            obj.put("buf", buf);
//            data.put("data", obj);

            out.write(obj.toString().getBytes("utf-8"));
            
            out.flush();
            
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            System.out.println(sb.toString());
            
            JSONParser parse = new JSONParser();
            JSONObject object = (JSONObject) parse.parse(sb.toString());
            String stat = (String) object.get("status");
            String msg = (String) object.get("msg");
            
            if ("400".equals(stat)) {
            	resObj.put("status", "400");
            	resObj.put("nick", (String) object.get("nick"));
            	resObj.put("characterid", (String) object.get("characterid"));
            	resObj.put("msg", msg);
            	
            	return resObj.toJSONString();
            }
            
            if (StringUtils.isNotEmpty(msg)) {
            	resObj.put("msg", msg);
            	return resObj.toJSONString();
            }

            if (!"N".equals(buf)) {
            	String bufStat = ((String) object.get("stat")).replaceAll("\n|\t", "").trim();
            	String dam = ((String) object.get("dam")).replaceAll("\n|\t", "").trim();
            	String jumsu = ((String) object.get("jumsu")).replaceAll("\n|\t", "").trim();
            	
            	resObj.put("bufStat", "스탯 : " + bufStat);
            	resObj.put("dam", "물/마/독 : " + dam);
            	resObj.put("jumsu", "버프점수" + jumsu);
            } else {
            	String sec25 = (String) object.get("sec25");
            	String sec40 = (String) object.get("sec40");
            	
            	sec25 = numberToKorean(sec25);
            	sec40 = numberToKorean(sec40);
            	
            	resObj.put("sec25", "1시 25초 - " + sec25);
            	resObj.put("sec40", "1시 40초 - " + sec40);
            }
            resObj.put("status", "200");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {}
            }
        }

        return resObj.toJSONString();
    }

	public String encodeURIComponent(String component)   {
        String result = null;      

        try {
            result = URLEncoder.encode(component, "UTF-8");
        } catch (Exception e) {
            result = component;
        }

        return result;
    }
	
	// 캐릭터 고유 아이디 조회
    public String getCharacterId(String server, String name) {
        
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        
        try {
            
            name = this.encodeURIComponent(name);
            
            String a = NEOPLE + "servers/" + server + "/characters?characterName="+ name +"&limit=10&wordType=match&apikey="+ API_KEY;
            
            
            URL url = new URL(a);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            
            conn.setRequestMethod("GET");
            
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            
            JSONParser parse = new JSONParser();
            JSONObject obj = (JSONObject)parse.parse(sb.toString());
            JSONArray jsonArr = (JSONArray) obj.get("rows");
            if(!obj.get("rows").toString().trim().equals("[]")) {
                obj = (JSONObject) jsonArr.get(0);
                sb.setLength(0);
                sb.append(obj.get("characterId").toString());
            } else {
                sb.setLength(0);
                return "x";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    
                }
            }
        }
        
        
        
        return sb.toString();
    }
    
    public static String numberToKorean(String number){
        number = number.replaceAll(",", "");
        String[] unitWords    = {"만", "억", "조", "경"};

        Stack<String> stack = new Stack<String>();
        int count = 0;
        int count2 = 0;
        for (int i = number.length()-1; i >= 0; i--) {
            
            stack.push(number.charAt(i)+"");
            count2++;
            if (count2 % 4 == 0 && i != 0) {
                stack.push(unitWords[count]);
                count++;
            }
        }
        StringBuilder sb = new StringBuilder();
        
        while(!stack.empty()) {
            sb.append(stack.pop());
        }
        
        return sb.toString();
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping("/puppTest")
    @ResponseBody
    public String puppTest(@RequestParam String server, @RequestParam String name) throws UnsupportedEncodingException {
        JSONObject resObj = new JSONObject();
        
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL("http://localhost:9999/");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "applitcation/json");
            conn.setRequestProperty("Content-type", "application/json");
            
            OutputStream out = conn.getOutputStream();
            JSONObject obj = new JSONObject();
            JSONObject data = new JSONObject();
            
            obj.put("nick", name);
            obj.put("characterid", server);

            out.write(obj.toString().getBytes("utf-8"));
            
            out.flush();
            
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            System.out.println(sb.toString());
            
            JSONParser parse = new JSONParser();
            JSONObject object = (JSONObject) parse.parse(sb.toString());
            String stat = (String) object.get("status");
            
            if ("400".equals(stat)) {
            	resObj.put("status", "400");
            	resObj.put("nick", (String) object.get("nick"));
            	resObj.put("characterid", (String) object.get("characterid"));
            	resObj.put("msg", (String) object.get("msg"));
            	
            	return resObj.toJSONString();
            }
            
            String sec25 = (String) object.get("sec25");
            String sec40 = (String) object.get("sec40");
            
            
            sec25 = numberToKorean(sec25);
            sec40 = numberToKorean(sec40);
            
            resObj.put("sec25", "1시 25초 - " + sec25);
            resObj.put("sec40", "1시 40초 - " + sec40);
            resObj.put("status", "200");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {}
            }
        }

        return resObj.toJSONString();
    }
}
