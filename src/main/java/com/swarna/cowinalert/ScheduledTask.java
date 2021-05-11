package com.swarna.cowinalert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.net.www.http.HttpClient;

@Component
public class ScheduledTask {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedDelay = 50000)
    public void reportCurrentTime() throws Exception {
        checkCenters();
    }

    @Scheduled(fixedDelay = 21600000)
    public void sendServiceUp() throws Exception {
        log.info("Sending service status");
        BaseTest.sendmail("Jaagte raho");
    }

    public static String sendGetRequest(String url, Map<String, String> header) throws IOException {
        HttpURLConnection connection = null;
        String response = null;
        URL restUrl = null;
        InputStream is = null;
        try {
            if(url.contains("\"")) {
                url = url.replace("\"","");
            }
            restUrl = new URL(url);
            connection = (HttpURLConnection) restUrl.openConnection();
            connection.setRequestMethod("GET");
            if(header != null) {
                for (String key : header.keySet()) {
                    connection.setRequestProperty(key,  header.get(key));
                }
            }
            connection.setRequestProperty("X-SESSION","this is test session");
            if(header == null || header == null || !header.containsKey("X-VERSION"))
                connection.setRequestProperty("X-VERSION","5.0.1");
            is = new BufferedInputStream(connection.getInputStream());
            response = convertStreamToString(is);
            if(connection.getResponseCode() >= 400) {
                throw new RuntimeException(response);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (IOException e) {
            is = new BufferedInputStream(connection.getErrorStream());
            try {
                response = convertStreamToString(is);
                log.error("Error Response:"+response);
            } catch (IOException e1) {
                log.error(e1.getMessage(), e1);
            }
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return response;
    }

    private static String convertStreamToString(InputStream is) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw e;
            }
        }

        return sb.toString();
    }


    private void checkCenters() throws Exception {

        String districtId = "725";
        String pattern = "dd/MM/yyyy";


        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String date = df.format(today);
        String baseURI = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?" +
                "district_id=725&date=" + date;
        Map<String, String> header = new HashMap();
        header.put("User-Agent", "PostmanRuntime/7.6.0");
        String res = sendGetRequest(baseURI, header);
        ObjectMapper mapper = new ObjectMapper();
        ResponseCowin c1 = mapper.readValue(res, ResponseCowin.class);

        StringBuilder sb = new StringBuilder();
        for (Center c : c1.getCenters()) {
            for (Session s : c.getSessions()) {
                if (s.getAvailableCapacity() > 0) {
                    String str = c.getName() + " " + s.getDate() + " " + s.getAvailableCapacity() + "For age: "+s.getMinAgeLimit();
                    System.out.println(str);
                    sb.append(str).append("\n");

                }
            }
        }

        if(!sb.toString().isEmpty()){
            BaseTest.sendmail(sb.toString());
        }

    }
}