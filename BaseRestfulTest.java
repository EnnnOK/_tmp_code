package asses.plan;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.bojun.utils.StringUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Model:
 * Description:
 * Author: 张立新
 * created: 2019/5/14
 */
public class BaseRestfulTest {
    public static HttpClient CLIENT = HttpClientBuilder.create().build();
    public static Map<String, String> LOG_PARAM = new HashMap<>();
    public static String BASE_URL = "http://localhost:8001/";
    public static String TOKEN = "";
    public static String SYS_GUID = "";

    static {
        LOG_PARAM.put("accountNo", "100000");
        LOG_PARAM.put("passwords", "12345678");
        LOG_PARAM.put("organizationId", "1002");
    }

    @BeforeClass
    public static void login() throws IOException{
        HttpPost request = new HttpPost(BASE_URL + "/login/loginByPwd");
        setHeader(request);
        request.setEntity(new StringEntity(JSONUtils.toJSONString(LOG_PARAM)));
        HttpResponse execute = CLIENT.execute(request);
        String response = EntityUtils.toString(execute.getEntity());
        Map<String, Object> map = (Map<String, Object>) JSONUtils.parse(response);
        if (validateResponse(map)) {
            Map<String, String> tokenMap = (Map<String, String>) map.get("data");
            TOKEN = tokenMap.get("token");
            assert !StringUtil.isEmpty(TOKEN);
        } else {
            assert false;
        }
    }

    @AfterClass
    public static void logout() throws IOException {
        HttpPost request = new HttpPost(BASE_URL + "/login/loginOut");
        setHeader(request);
        HttpResponse execute = CLIENT.execute(request);
        String response = EntityUtils.toString(execute.getEntity());
        Map<String, Object> map = (Map<String, Object>) JSONUtils.parse(response);
        assert map.get("code").toString().equals("200");
    }

    public static boolean validateResponse(Map<String, Object> map) {
        return map.get("code") != null
                && map.get("code").toString().equalsIgnoreCase("200")
                && map.get("data") != null;
    }

    public Map<String, Object> parseResp(HttpResponse response) throws IOException {
        String responseString = EntityUtils.toString(response.getEntity());
        Map<String, Object> respMap = (Map<String, Object>) JSON.parse(responseString);
        if (!validateResponse(respMap)) {
            assert false;
            return null;
        }
        return respMap;
    }

    public static void setHeader(HttpRequestBase request) {
        request.addHeader("Content-Type", "application/json");
        request.addHeader("token", TOKEN);
    }

}
