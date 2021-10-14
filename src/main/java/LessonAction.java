import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

/**
 * @Classname LessonAction
 * @Description TODO
 * @Date 2021/10/10 3:25
 * @Created by 晨曦
 */
public class LessonAction extends Action {
    private String uuid;
    private String dateFormate;
    private String recruitAndCourseId;
    String recruitId;
    String courseId;

    public LessonAction(String uuid, String dateFormate,String recruitAndCourseId) {
        super("https://studyservice.zhihuishu.com/login/gologin?fromurl=https%3A%2F%2Fstudyh5.zhihuishu.com%2FvideoStudy.html%23%2FstudyVideo%3FrecruitAndCourseId%3D42515b5845524258454a585859465d42");
        this.uuid = uuid;
        this.dateFormate = dateFormate;
        this.recruitAndCourseId = recruitAndCourseId;
    }
    //[recruitId,lessonId,smallLessonId,lastViewVideoId,chapterId,studyStatus,playTimes,totalStudyTime,Position]


    public void preExe(OkHttpClient okHttpClient,String chapterId,String lessonId,String videoId) throws IOException {
        FormBody form1 = new FormBody.Builder()
                .add("ccCourseId", this.courseId)
                .add("chapterId", chapterId)
                .add("isApply", "1")
                .add("lessonId",lessonId)
                .add("recruitId", this.recruitId)
                .add("videoId", videoId)
                .add("uuid", this.uuid)
                .add("dateFormate", dateFormate).build();
        final Request request1 = new Request.Builder()
                .url("https://studyservice.zhihuishu.com/learning/prelearningNote")
                .post(form1)
                .build();
        Response execute1 = okHttpClient.newCall(request1).execute();
        System.out.println(execute1.body().string());
    }

    public void goExe(OkHttpClient okHttpClient,String watchPoint, String ev, String learningTokenId, String courseId) throws IOException {
        FormBody form = new FormBody.Builder()
                .add("watchPoint", watchPoint)
                .add("ev", ev)
                .add("learningTokenId", learningTokenId)
                .add("courseId", courseId)
                .add("uuid", uuid)
                .add("dateFormate", dateFormate).build();
        final Request request = new Request.Builder()
                .url("https://studyservice.zhihuishu.com/learning/saveDatabaseIntervalTime")
                .post(form)
                .build();
        Response execute = okHttpClient.newCall(request).execute();
        System.out.println(execute.body().string());
    }

    public JSONArray videoList(OkHttpClient okHttpClient) throws IOException {
        FormBody form = new FormBody.Builder()
                .add("recruitAndCourseId",this.recruitAndCourseId)
                .add("uuid",this.uuid)
                .add("dateFormate",this.dateFormate).build();
        final Request request = new Request.Builder()
                .url("https://studyservice.zhihuishu.com/learning/videolist")
                .post(form)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject jsonObject = (JSONObject) JSONObject.parse(response.body().string());
        this.recruitId = jsonObject.getJSONObject("data").getString("recruitId");
        this.courseId = jsonObject.getJSONObject("data").getString("courseId");
        return jsonObject.getJSONObject("data").getJSONArray("videoChapterDtos");
    }

    public static String evCreator(String[] params){
        StringBuilder sb = new StringBuilder();
        for (String s:params){
            sb.append(s);sb.append(";");
        }
        return Encrypt.encrypt(sb.substring(0,sb.length()-1));
    }
}
