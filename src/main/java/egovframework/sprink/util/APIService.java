package egovframework.sprink.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
 
import org.json.simple.JSONObject;
 
public class APIService {
 
	public static void main(String args[]) {
		APIService service = new APIService();
		String body = service.apiServiceTest();
		System.out.println(body);
	}
    public String apiServiceTest() {
 
        URL url = null;
        URLConnection connection = null;
        StringBuilder responseBody = new StringBuilder();
 
        try {
            // URL 지정
            url = new URL("http://svc.saltlux.ai:31781");
            connection = url.openConnection();
            // Header 정보 지정
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);
 
            JSONObject jsonBody = new JSONObject();
            // 사용자 키
            jsonBody.put("key", "e0f4e20e-cd64-4434-b686-b1983441a5c1");
            // 서비스 ID
            jsonBody.put("serviceId", "01139773605");
 
            // 서비스에서 필요로 하는 parameter
            JSONObject argument = new JSONObject();
            argument.put("question", "아버지가방에들어가셨습니다.");
            jsonBody.put("argument", argument);
 
            BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
 
            bos.write(jsonBody.toJSONString().getBytes(StandardCharsets.UTF_8));
            bos.flush();
            bos.close();
 
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line = null;
            while ((line = br.readLine()) != null) {
                responseBody.append(line);
            }
            br.close();
 
        } catch (Throwable e) {
            System.out.println(e.toString());
        }
 
        return responseBody.toString();
    }
 
}
