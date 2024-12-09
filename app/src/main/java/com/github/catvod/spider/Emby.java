package com.github.catvod.spider;


import android.content.Context;

import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;

import android.util.Base64;

import android.text.TextUtils;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;


public class Emby extends Spider {
    private String url;
    private String playUrl;

    @Override
    public void init(Context context, String extend) throws Exception {
        super.init(context);
        try {
            String s = new String(Base64.decode(extend, Base64.DEFAULT | Base64.URL_SAFE | Base64.NO_WRAP), "UTF-8");
            JSONObject jsonObject = new JSONObject(s);
            int embyId = jsonObject.getInt("id");
            String api = jsonObject.getString("api");
            String token = jsonObject.getString("token");
            if (!TextUtils.isEmpty(token)) {
                url = api + "/emby/" + token + "?embyId=" + embyId;
                playUrl = api + "/emby-play/" + token;
            } else {
                url = api + "/emby" + "?embyId=" + embyId;
                playUrl = api + "/emby-play";
            }

        } catch (Exception ignored) {

        }
    }

    public String homeContent(boolean filter) throws Exception {
        return OkHttp.string(url);
    }

    public String homeVideoContent() throws Exception {
        String reqUrl = url + "&ids=recommend";
        return OkHttp.string(reqUrl);
    }

    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) throws Exception {
        String reqUrl = url + "&t=" + tid + (TextUtils.isEmpty(pg) ? "" : "&pg=" + pg) + (!extend.containsKey("sort") ? "" : "&sort=" + extend.get("sort"));
        return OkHttp.string(reqUrl);
    }


    public String detailContent(List<String> ids) throws Exception {
        String reqUrl = url + "&ids=" + ids.get(0);
        return OkHttp.string(reqUrl);
    }

    public String searchContent(String key, boolean quick) throws Exception {
        String reqUrl = url + "&wd=" + URLEncoder.encode(key);
        return OkHttp.string(reqUrl);
    }

    public String playerContent(String flag, String id, List<String> vipFlags) throws Exception {
        String reqUrl = playUrl + "&id=" + id;
        return OkHttp.string(reqUrl);
    }

}
