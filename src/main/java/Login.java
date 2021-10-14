import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

/**
 * @Classname Login
 * @Description TODO
 * @Date 2021/10/9 22:18
 * @Created by 晨曦
 */
public class Login {
    String code;
    String password;
    String schoolId;
    String uuid;
    private final Map<String,Map<String,Cookie>> cookieMap = new HashMap<>();
    OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {
        @Override
        public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
            for(Cookie cookie:list){
                if (cookie.name().equals("CASTGC")||cookie.name().equals("CASLOGC")){
                    Map<String,Cookie> map = new HashMap<>();
                    map.put(cookie.name(),cookie);
                    if (cookieMap.containsKey("all")){
                        cookieMap.get("all").put(cookie.name(),cookie);
                    }else {
                        cookieMap.put("all",map);
                    }
                    continue;
                }
                if (cookieMap.containsKey(httpUrl.host())){
                    cookieMap.get(httpUrl.host()).put(cookie.name(),cookie);
                }else {
                    Map<String,Cookie> map = new HashMap<>();
                    map.put(cookie.name(),cookie);
                    cookieMap.put(httpUrl.host(),map);
                }
            }
        }

        @NotNull
        @Override
        public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
            List<Cookie> cookies = new ArrayList<>();
            if (cookieMap.containsKey(httpUrl.host())){
                for (Cookie cookie : cookieMap.get(httpUrl.host()).values()){
                    cookies.add(cookie);
                }
            }
            if (cookieMap.containsKey("all")){
                cookies.addAll(cookieMap.get("all").values());
            }
            return cookies;
        }
    }).build();

    public Login(String code, String password, String schoolId,boolean isPhonePassword) throws IOException {
        this.code = code;
        this.password = password;
        this.schoolId = schoolId;
        FormBody formBody;
        if (isPhonePassword){
            formBody = new FormBody.Builder()
                    .add("account",code)
                    .add("password",password)
                    .build();
        }else {

            formBody = new FormBody.Builder()
                    .add("code",code)
                    .add("password",password)
                    .add("schoolId",schoolId).build();
        }


        final Request loginRequest = new Request.Builder()
                .url(isPhonePassword?"https://passport.zhihuishu.com/user/validateAccountAndPassword":"https://passport.zhihuishu.com/user/validateCodeAndPassword")
                .post(formBody)
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36")
                .addHeader("Referer","https://passport.zhihuishu.com/login?service=https%3A%2F%2Fonlineservice.zhihuishu.com%2Flogin%2Fgologin%3Ffromurl%3Dhttps%253A%252F%252Fonlineh5.zhihuishu.com%252FonlineWeb.html%2523%252FstudentIndex")
                .build();
        final Call call = okHttpClient.newCall(loginRequest);
        Response response = call.execute();
        JSONObject obj = (JSONObject) JSONObject.parse(response.body().string());
        System.out.println(obj.toJSONString());
        uuid = obj.getString("uuid");
        formBody = new FormBody.Builder().add("uuid",uuid).build();
        final Request checkRequest = new Request.Builder()
                .url("https://appcomm-user.zhihuishu.com/app-commserv-user/userInfo/checkNeedAuth")
                .post(formBody)
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36")
                .addHeader("Referer","https://passport.zhihuishu.com/")
                .build();
        okHttpClient.newCall(checkRequest).execute();
        final Request login1Request = new Request.Builder()
                .url("https://passport.zhihuishu.com/login?pwd="+obj.getString("pwd")+"&service=https://onlineservice.zhihuishu.com/login/gologin?fromurl=https%3A%2F%2Fonlineh5.zhihuishu.com%2FonlineWeb.html%23%2FstudentIndex")
                .get()
                .addHeader("sec-ch-ua","\"Chromium\";v=\"94\", \"Google Chrome\";v=\"94\", \";Not A Brand\";v=\"99\"")
                .addHeader("sec-ch-ua-mobile","?0")
                .addHeader("sec-ch-ua-platform","\"Windows\"")
                .addHeader("Sec-Fetch-Dest","document")
                .addHeader("Sec-Fetch-Mode","navigate")
                .addHeader("Sec-Fetch-Site","same-origin")
                .addHeader("Sec-Fetch-User","?1")
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36")
                .addHeader("Referer","https://passport.zhihuishu.com/login?service=https%3A%2F%2Fonlineservice.zhihuishu.com%2Flogin%2Fgologin%3Ffromurl%3Dhttps%253A%252F%252Fonlineh5.zhihuishu.com%252FonlineWeb.html%2523%252FstudentIndex")
                .build();
        Response response1 = okHttpClient.newCall(login1Request).execute();
    }

    public void login(Action action) throws IOException {
        action.goLogin(okHttpClient);
        System.out.println(JSON.toJSONString(cookieMap));
    }
}
