const scriptName = "test";

const kalingModule = require('kaling').Kakao();
const Kakao = new kalingModule;
Kakao.init('9853999f1b5e8999c8c39ff5997edf9f');
Kakao.login('qwqws9@naver.com','gnfkqh12');
/**
 * (string) room
 * (string) sender
 * (boolean) isGroupChat
 * (void) replier.reply(message)
 * (boolean) replier.reply(room, message, hideErrorToast = false) // 전송 성공시 true, 실패시 false 반환
 * (string) imageDB.getProfileBase64()
 * (string) packageName
 */
function response(room, msg, sender, isGroupChat, replier, imageDB, packageName) {
  if (msg == "/테스트") {
    //var u = Utils.getWebText("http://dundam.xyz/searchActionTest.jsp?server=cain&name=씨리야");
    //replier.reply(u);
	  var msgArr = msg.split(' ');
	  if (msgArr.length != 3) {
		  replier.reply("명령어 오류 !");
		  return;
	  }
	  replier.reply("검색 중...");
	  // http://localhost:8080/neople/equip/%EC%B9%B4%EC%9D%B8?name=%EC%94%A8%EB%A6%AC%EC%95%BC&imgYn=Y
	  var v = org.jsoup.Jsoup.connect("http://104.196.235.254/neople/equip/" + msgArr[1] + "?name="+ msgArr[2] +"&imgYn=Y").ignoreContentType(true).get().text();
	  var json = JSON.parse(v);
	  var server = json.server;
	  var id = json.id;
	  var img = json.img;
	  
	  Kakao.send(room, {
		  link_ver: "4.0",
		  template_id: 33605,
		  template_args: {
		  img: json.img,
		  profile: json.server + ' - ' + json.id,
		  sample : '여기는 뭐가 들어가면 좋을까?',
		  button1 : '버튼1',
		  button2 : '버튼2'
		}}, "custom");
  }
}

//아래 4개의 메소드는 액티비티 화면을 수정할때 사용됩니다.
function onCreate(savedInstanceState, activity) {
  var textView = new android.widget.TextView(activity);
  textView.setText("Hello, World!");
  textView.setTextColor(android.graphics.Color.DKGRAY);
  activity.setContentView(textView);
}

function onStart(activity) {}

function onResume(activity) {}

function onPause(activity) {}

function onStop(activity) {}