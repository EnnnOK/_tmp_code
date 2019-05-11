package *.*;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.bojun.utils.StringUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Model:
 * Description:
 * Author: 张立新
 * created: 2019/5/11
 */
public class StaffRESTFulTest {
    private static HttpClient CLIENT = HttpClientBuilder.create().build();
    private static Map<String, String> LOG_PARAM = new HashMap<>();
    private static String BASE_URL = "http://localhost:8001/";

    private static String TOKEN = "";
    private static String SYS_GUID = "";

    static {
        LOG_PARAM.put("accountNo", "100000");
        LOG_PARAM.put("passwords", "12345678");
        LOG_PARAM.put("organizationId", "10001");
    }

    private static String STF_NAME = "TEST_NAME";
    private static String STF_ID_CD = "444222199410186666";
    private static String STF_TITLE = "E____";
    private static String STF_ORG_NAME = "test_stf_org_name";
    private static String STF_ORG_ID = "test_stf_org_id";
    private static String STF_DPT_NAME = "test_dpt_name";
    private static String STF_DPT_ID = "test_dpt_id";
    private static String IS_STAFF = "1";

    @Test
    public void T1_log() throws IOException {
        HttpPost request = new HttpPost(BASE_URL + "/login/loginByPwd");
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(JSONUtils.toJSONString(LOG_PARAM)));
        HttpResponse execute = CLIENT.execute(request);
        String response = EntityUtils.toString(execute.getEntity());
        Map<String, Object> map = (Map<String, Object>) JSONUtils.parse(response);
        if (validateResponse(map)) {
            Map<String, String> tokenMap = (Map<String, String>) map.get("data");
            TOKEN = tokenMap.get("token");
            assert (!StringUtil.isEmpty(TOKEN));
        } else {
            assert (false);
        }
    }

    @Test
    public void T2_add() throws IOException {
        HttpPost request = new HttpPost(BASE_URL + "service/staff/add");
        request.addHeader("Content-Type", "application/json");
        request.addHeader("token", TOKEN);
        Map<String, String> map = new HashMap<>();
        map.put("staffName", STF_NAME);
        map.put("idCardNo", STF_ID_CD);
        map.put("title", STF_TITLE);
        map.put("organName", STF_ORG_NAME);
        map.put("organId", STF_ORG_ID);
        map.put("departName", STF_DPT_NAME);
        map.put("departId", STF_DPT_ID);
        map.put("isStaff", IS_STAFF);
        request.setEntity(new StringEntity(JSON.toJSONString(map)));
        HttpResponse resp = CLIENT.execute(request);
        Map<String, Object> respMap = parseResp(resp);
        Map<String, Object> dataMap = (Map<String, Object>) respMap.get("data");
        SYS_GUID = dataMap.get("sysGuid").toString();
        assert (!StringUtil.isEmpty(SYS_GUID));
    }

    @Test
    public void T3_chk() throws IOException {
        HttpPost request = new HttpPost(BASE_URL + "service/staff/check");
        request.addHeader("Content-Type", "application/json");
        request.addHeader("token", TOKEN);
        Map<String, String> map = new HashMap<>();
        map.put("idCardNo", STF_ID_CD);
        request.setEntity(new StringEntity(JSON.toJSONString(map)));
        HttpResponse resp = CLIENT.execute(request);
        Map<String, Object> respMap = parseResp(resp);
        Map<String, Object> dataMap = (Map<String, Object>) respMap.get("data");
        assert (dataMap.get("organName").toString().equals(STF_ORG_NAME));
    }

    @Test
    public void T4_get() throws IOException {
        if (StringUtil.isEmpty(TOKEN) || StringUtil.isEmpty(SYS_GUID)) {
            assert (false);
            return;
        }
        HttpGet request = new HttpGet(BASE_URL + "service/staff/getById?sysGuid=" + SYS_GUID);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("token", TOKEN);
        HttpResponse resp = CLIENT.execute(request);
        Map<String, Object> respMap = parseResp(resp);
        Map<String, Object> dataMap = (Map<String, Object>) respMap.get("data");
        assert (dataMap.get("organName").toString().equals(STF_ORG_NAME));
    }

    @Test
    public void T5_del() throws IOException {
        HttpPost request = new HttpPost(BASE_URL + "service/staff/delete");
        request.addHeader("Content-Type", "application/json");
        request.addHeader("token", TOKEN);
        Map<String, String> addMap = new HashMap<>();
        addMap.put("sysGuid", SYS_GUID);
        request.setEntity(new StringEntity(JSONUtils.toJSONString(addMap)));
        HttpResponse resp = CLIENT.execute(request);
        Map<String, Object> respMap = parseResp(resp);
        Map<String, Object> dataMap = (Map<String, Object>) respMap.get("data");
        assert (true);

    }

    boolean validateResponse(Map<String, Object> map) {
        return map.get("code") != null
                && map.get("code").toString().equalsIgnoreCase("200")
                && map.get("data") != null;
    }

    Map<String, Object> parseResp(HttpResponse response) throws IOException {
        String responseString = EntityUtils.toString(response.getEntity());
        Map<String, Object> respMap = (Map<String, Object>) JSON.parse(responseString);
        if (!validateResponse(respMap)) {
            assert (false);
            return null;
        }
        return respMap;
    }
}
