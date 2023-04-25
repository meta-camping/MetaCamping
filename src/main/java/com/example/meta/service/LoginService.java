package com.example.meta.service;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Log4j2
@Service
public class LoginService {

    public String getAccessToken(String code) throws IOException {
        //인가코드로 토큰 받기
        String accessToken = "";
        String refreshToken = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            log.info("conn27L = " + conn);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            //CLITENT_ID는 본인 REST API 키 입력하면 됨
            sb.append("&client_id=dd8d20f4bcb19eeccbc530903c95dded");  //본인 걸로 수정하기
            sb.append("&redirect_uri=http://localhost:8080/login");  //본인 걸로 수정하기
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("responseCode44L = " + responseCode);

            //JSON 파싱
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("responseBody54L = " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON 파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    public HashMap<String, String> getUserInfo(String accessToken) throws IOException {
        HashMap<String, String> userInfo = new HashMap<String, String>();
        String reqUrl = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            log.info("responseCode82L = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("responseBody92L = " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            log.info("properties99L = " + properties);
            log.info("kakaoAccount100L = " + kakaoAccount);

            String profile_nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String account_email = kakaoAccount.getAsJsonObject().get("email").getAsString();

            userInfo.put("account_email", account_email);
            userInfo.put("profile_nickname", profile_nickname);

            log.info("userInfo106L = " + userInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public void logout(String accessToken) {
        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            int responseCode = conn.getResponseCode();
            log.info("responseCode125L = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                result += line;
                log.info("로그아웃이 되었습니다");
            }
            log.info("result136L = " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}