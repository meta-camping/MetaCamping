package com.example.firstproject.controller;

import com.example.firstproject.entity.Dust;
import com.example.firstproject.repository.DustRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Log4j2
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class APIController {
    @Value("${env.serviceKey}")
    private String serviceKey;
    private final DustRepository dustRepository;

    @GetMapping("/weatherforecast")
    public String restApiGetWeatherNow(@RequestParam String coordinate_x,@RequestParam String coordinate_y) throws Exception {
        // 현재 시간
        LocalTime now = LocalTime.now();

        // 오늘 날짜
        Date date = new Date();

        // 포맷 정의하기
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH");
        SimpleDateFormat formatterDate = new SimpleDateFormat("yy년 MM월 dd일 E요일 a h시 mm분 ");

        // 포맷 적용하기
        String formatedNow = now.format(formatterTime);
        String formatedToday = formatterDate.format(date); // 리액트로 보내줄 오늘 날짜
        String nowTime = formatedNow + "00"; // 데이터 조회에 사용할 시간

        //yyyyMMdd 형식의 어제 날짜 구하기
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DATE, -1); // 오늘날짜로부터 -1
        String yesterday = sdf.format(c1.getTime()); // String으로 저장

        //yyyyMMdd 형식의 오늘 날짜 구하기
        String today = sdf.format(date); // String으로 저장

        //yyyyMMdd 형식의 내일 날짜 구하기
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DATE, +1); // 오늘날짜로부터 +1
        String tomorrow = sdf.format(c2.getTime()); // String으로 저장

        log.info("yesterday: " + yesterday); // 어제 날짜 출력
        log.info("today: " + today); // 오늘 날짜 출력
        log.info("tomorrow: " + tomorrow); // 내일 날짜 출력

        //날씨를 가져오기 위한 url
        String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"
                + "?serviceKey=" + serviceKey
                + "&dataType=JSON"            // JSON, XML
                + "&numOfRows=600"             // 페이지 ROWS
                + "&pageNo=1"                 // 페이지 번호
                + "&base_date=" + yesterday   // 발표일자
                + "&base_time=2300"           // 발표시각
                + "&nx=" + coordinate_x       // 예보지점 X 좌표
                + "&ny=" + coordinate_y;     // 예보지점 Y 좌표


        HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");

        JSONObject jsonObj = new JSONObject(); // 데이터 가공을 위한 객체 생성

        jsonObj.put("result", resultMap);

        JSONObject jsonObj2 = new JSONObject(); // 실제로 값을 리턴할 객체 생성

        jsonObj2.put("todayTime", formatedToday); // 전달한 객체로 TodayTime 추가

        JSONObject todayData = new JSONObject(); // 실제로 값을 리턴할 객체 생성
        JSONObject tomorrowData = new JSONObject(); // 실제로 값을 리턴할 객체 생성

        Map<String, String> todayDataSub = new HashMap<>(); //  오늘의 각 시간별 데이터를 저장할 객체
        Map<String, String> tomorrowDataSub = new HashMap<>(); // 내일의 각 시간별 데이터를 저장할 객체

        JSONArray items = jsonObj.getJSONObject("result").getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
        log.info(items);

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String fcstDate = item.getString("fcstDate");
                String fcstTime = item.getString("fcstTime");
                String category = item.getString("category");
                String fcstValue = item.getString("fcstValue");

                if (fcstDate.equals(today) && category.equals("TMX")) {
                    int TMX = (int) Double.parseDouble(fcstValue); // 실수로 제공되는 TMX를 int로 변환
                    jsonObj2.put("TMX", TMX); // 전달한 객체로 TMX 추가
                    log.info("일 최고 기온 예보 시간: " + fcstTime);
                }

                if (fcstDate.equals(today) && category.equals("TMN")) {
                    int TMN = (int) Double.parseDouble(fcstValue); // 실수로 제공되는 TMN을 int로 변환
                    jsonObj2.put("TMN", TMN); // 전달한 객체로 TMN 추가
                    log.info("일 최저 기온 예보 시간: " + fcstTime);
                }

                //현재 시간과 일치하는 데이터 저장
                if (fcstTime.equals(nowTime) && fcstDate.equals(today)) {
                    switch (category) {
                        case "TMP":
                            String TMP = fcstValue;
                            jsonObj2.put("TMP", TMP); // 전달한 객체로 TMP 추가
                            break;
                        case "REH":
                            String REH = fcstValue;
                            jsonObj2.put("REH", REH); // 전달한 객체로 REH 추가
                            break;
                        case "POP":
                            String POP = fcstValue;
                            jsonObj2.put("POP", POP); // 전달한 객체로 POP 추가
                            break;
                        case "SKY":
                            String SKY = fcstValue;
                            jsonObj2.put("SKY", SKY); // 전달한 객체로 SKY 추가
                            break;
                        case "PTY":
                            String PTY = fcstValue;
                            jsonObj2.put("PTY", PTY); // 전달한 객체로 PTY 추가
                            break;
                    }
                }

                if (fcstDate.equals(today)) {
                    todayDataSub.put(category, fcstValue);
                    todayData.put(fcstTime, todayDataSub);
                } else if (fcstDate.equals(tomorrow)) {
                    tomorrowDataSub.put(category, fcstValue);
                    tomorrowData.put(fcstTime, tomorrowDataSub);
                }
            }
        jsonObj2.put("Today", todayData); // 전달한 객체로 Today data 추가
        jsonObj2.put("Tomorrow", tomorrowData); // 전달한 객체로 Tomorrow data 추가
        System.out.println("jsonObj2: " + jsonObj2);
        return jsonObj2.toString();
    }
    //미세먼지 전체 데이터 찾아서 리턴
    @GetMapping("/dust/showAllList")
    public List<Dust> showAllList() throws Exception {
            //모든 Article을 가져온다!
            List<Dust> dustEntityList = dustRepository.findAll();

            return dustEntityList;
    }

    //도시이름으로 데이터 찾아서 리턴
    @GetMapping("/dust/showList")
    public List<Dust> showBySidoName(@RequestParam String sido_name) {
        List<Dust> sidoEntityList = dustRepository.findAllBySidoName(sido_name);

        return sidoEntityList;
    }

    //지역이름으로 데이터 찾아서 리턴
    @GetMapping("/dust/searchStation")
    public List<Dust> showByStationName(@RequestParam String station_name) {
        List<Dust> EntityList = dustRepository.findAllByStationName(station_name);
        return EntityList;
    }

    //읍면동을 가지고 TM 기준좌표 조회
    @GetMapping("/dust/convert")
    public String convertToTM(@RequestParam String station_name) throws Exception {
        String stationName = URLEncoder.encode(station_name);

        String url = "http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getTMStdrCrdnt"
                + "?serviceKey=" + serviceKey
                + "&returnType=json"            // JSON, XML
                + "&numOfRows=100"             // 페이지 ROWS
                + "&pageNo=1"                 // 페이지 번호
                + "&umdName=" + stationName;        // 읍면동명

        HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");

        JSONObject jsonObj = new JSONObject();

        jsonObj.put("result", resultMap);

        return jsonObj.toString();
    }

    //현재 위치에서 가까운 근처 측정소 조회
    @GetMapping("/dust/search/measuringstation")
    public String searchMeasuringStation(@RequestParam String tmX, @RequestParam String tmY) throws Exception {

        String url = "http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList"
                + "?serviceKey=" + serviceKey
                + "&returnType=json"            // JSON, XML
                + "&tmX=" + tmX             // 페이지 ROWS
                + "&tmY=" + tmY;             // 페이지 번호
        HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");

        JSONObject jsonObj = new JSONObject();

        jsonObj.put("result", resultMap);

        return jsonObj.toString();
//        try {
//            URL addr = new URL(url);
//
//            HttpURLConnection con = (HttpURLConnection)addr.openConnection();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//
//            String line = br.readLine();
//        }catch(Exception e){
//            System.out.println(e.getLocalizedMessage());
//        }
    }

    public HashMap<String, Object> getDataFromJson(String url, String encoding, String type, String jsonStr) throws Exception {
        boolean isPost = false;

        if ("post".equals(type)) {
            isPost = true;
        } else {
            url = "".equals(jsonStr) ? url : url + "?request=" + jsonStr;
        }

        return getStringFromURL(url, encoding, isPost, jsonStr, "application/json");
    }

    public HashMap<String, Object> getStringFromURL(String url, String encoding, boolean isPost, String parameter, String contentType) throws Exception {
        URL apiURL = new URL(url);

        HttpURLConnection conn = null;
        BufferedReader br = null;
        BufferedWriter bw = null;

        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        try {
            conn = (HttpURLConnection) apiURL.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);

            if (isPost) {
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", contentType);
                conn.setRequestProperty("Accept", "*/*");
            } else {
                conn.setRequestMethod("GET");
            }

            conn.connect();

            if (isPost) {
                bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                bw.write(parameter);
                bw.flush();
                bw = null;
            }

            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));

            String line = null;

            StringBuffer result = new StringBuffer();

            while ((line=br.readLine()) != null) result.append(line);

            ObjectMapper mapper = new ObjectMapper();

            resultMap = mapper.readValue(result.toString(), HashMap.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(url + " interface failed" + e.toString());
        } finally {
            if (conn != null) conn.disconnect();
            if (br != null) br.close();
            if (bw != null) bw.close();
        }

        return resultMap;
    }
}