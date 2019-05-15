package asses.plan;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Model:
 * Description:
 * Author: 张立新
 * created: 2019/5/14
 */
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class AssesPlanRESTfulTest extends BaseRestfulTest {

    public static final String ASSES_OBJ_ID = "test_obj_id";
    public static final String THEME_ID = "test_theme_id";
    public static final String THEME_NAME = "test_theme_name";
    public static final Integer DIMENSION = 1;
    public static final Integer ASSES_OBJ = 1;

    public static final List<String> INDICATORS = new LinkedList<>();
    public static final List<String> OBJECTS = new LinkedList<>();

    public static final List<String> INDICATORS_NEW = new LinkedList<>();
    public static final List<String> OBJECTS_NEW = new LinkedList<>();
    static {
        INDICATORS.add("123");
        INDICATORS.add("456");
        INDICATORS.add("789");

        OBJECTS.add("123");
        OBJECTS.add("456");
        OBJECTS.add("789");

        INDICATORS_NEW.add("2_123");
        INDICATORS_NEW.add("2_456");
        INDICATORS_NEW.add("2_789");

        OBJECTS_NEW.add("2_123");
        OBJECTS_NEW.add("2_456");
        OBJECTS_NEW.add("2_789");
    }

    @Test
    public void T1_add() throws IOException {
        HttpPost request = new HttpPost(BASE_URL + "asses/plan/add");
        setHeader(request);
        Map<String, Object> map = new HashMap<>();
        map.put("assesObjectGuid", ASSES_OBJ_ID);
        map.put("themeId", THEME_ID);
        map.put("themeName", THEME_NAME);
        map.put("dimension", DIMENSION);
        map.put("assesObject", ASSES_OBJ);
        request.setEntity(new StringEntity(JSON.toJSONString(map)));
        HttpResponse resp = CLIENT.execute(request);
        Map<String, Object> respMap = parseResp(resp);
        Map<String, Object> dataMap = (Map<String, Object>) respMap.get("data");
        SYS_GUID = dataMap.get("sysGuid").toString();
        assert SYS_GUID != null;
    }

    @Test
    public void T2_get() throws IOException {
        HttpGet request = new HttpGet(BASE_URL + "asses/plan/getById?sysGuid=" + SYS_GUID);
        setHeader(request);
        HttpResponse resp = CLIENT.execute(request);
        Map<String, Object> respMap = parseResp(resp);
        Map<String, Object> dataMap = (Map<String, Object>) respMap.get("data");
        assert dataMap.get("assesObjectGuid").toString().equals(ASSES_OBJ_ID);
    }

    @Test
    public void T3_update() throws IOException {
        HttpPost request = new HttpPost(BASE_URL + "asses/plan/update");
        setHeader(request);
        Map<String, Object> map = new HashMap<>();
        map.put("sysGuid", SYS_GUID);
        map.put("indicators", INDICATORS);
        map.put("objects", OBJECTS);
        request.setEntity(new StringEntity(JSON.toJSONString(map)));
        HttpResponse resp = CLIENT.execute(request);
        Map<String, Object> respMap = parseResp(resp);
        Map<String, Object> dataMap = (Map<String, Object>) respMap.get("data");
        assert SYS_GUID != null;
    }

    @Test
    public void T4_update() throws IOException {
        HttpPost request = new HttpPost(BASE_URL + "asses/plan/update");
        setHeader(request);
        Map<String, Object> map = new HashMap<>();
        map.put("sysGuid", SYS_GUID);
        map.put("indicators", INDICATORS_NEW);
        map.put("objects", OBJECTS_NEW);
        request.setEntity(new StringEntity(JSON.toJSONString(map)));
        HttpResponse resp = CLIENT.execute(request);
        Map<String, Object> respMap = parseResp(resp);
        Map<String, Object> dataMap = (Map<String, Object>) respMap.get("data");
        assert SYS_GUID != null;
    }

    @Test
    public void T5_del() throws IOException {
        HttpPost request = new HttpPost(BASE_URL + "asses/plan/delete");
        setHeader(request);
        Map<String, String> addMap = new HashMap<>();
        addMap.put("sysGuid", SYS_GUID);
        request.setEntity(new StringEntity(JSONUtils.toJSONString(addMap)));
        HttpResponse resp = CLIENT.execute(request);
        Map<String, Object> respMap = parseResp(resp);
        Map<String, Object> dataMap = (Map<String, Object>) respMap.get("data");
        assert true;
    }
}
