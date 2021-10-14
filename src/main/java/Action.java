import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @Classname Action
 * @Description TODO
 * @Date 2021/10/10 3:30
 * @Created by 晨曦
 */
public abstract class Action {
    String loginUrl;

    public Action(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public FormBody toFromBody(){
        return null;
    }
    public void goLogin(OkHttpClient okHttpClient) throws IOException {
        final Request requestF = new Request.Builder()
                .url(this.loginUrl)
                .get()
                .build();
        okHttpClient.newCall(requestF).execute();
    }
}
