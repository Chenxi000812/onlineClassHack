import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;

/**
 * @Classname ExamAction
 * @Description TODO
 * @Date 2021/10/11 21:27
 * @Created by 晨曦
 */
public class ExamAction extends Action{
    String uuid;
    String recruitId;
    String courseId;
    String schoolId;
    String authorization;
    public ExamAction(String recruitId,String courseId,String uuid,String schoolId) {
        super("https://studentexam.zhihuishu.com/studentExam/gologin/login?fromurl=https%3A%2F%2Fonlineexamh5new.zhihuishu.com%2FstuExamWeb.html%23%2FwebExamList%3FrecruitId%3D1ApEpExryI1%252FTTMywb%252FxBg%253D%253D");
        this.recruitId=recruitId;
        this.courseId=courseId;
        this.uuid = uuid;
        this.schoolId = schoolId;
    }

    public JSONArray contentList(OkHttpClient okHttpClient,String studentExamId,String examId)throws IOException{
        FormBody formBody = new FormBody.Builder()
                .add("recruitId",this.recruitId)
                .add("examId",examId)
                .add("studentExamId",studentExamId)
                .add("schoolId",this.schoolId)
                .add("courseId",this.courseId)
                .add("uuid",this.uuid).build();
        final Request request = new Request.Builder()
                .url("https://studentexam.zhihuishu.com/studentExam/student/doHomework")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject object = JSONObject.parseObject(response.body().string());
        return object.getJSONObject("rt").getJSONObject("examBase").getJSONArray("workExamParts");
    }

    public void sendAnswer(OkHttpClient okHttpClient,String stuExamId,String examId,String eid,Object answer,Integer questionType) throws IOException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("examId",examId);
        jsonObject.put("recruitId",this.recruitId);
        jsonObject.put("stuExamId",stuExamId);
        jsonObject.put("eid",eid);
        jsonObject.put("answer",answer);
        jsonObject.put("dataIds","");
        jsonObject.put("questionType",questionType);
        jsonArray.add(jsonObject);
        FormBody formBody = new FormBody.Builder()
                .add("stuExamAnswer",jsonArray.toJSONString())
                .add("recruitId",this.recruitId)
                .add("uuid",this.uuid)
                .build();
        final Request request = new Request.Builder()
                .url("https://studentexam.zhihuishu.com/studentExam/answer/saveStudentAnswer")
                .post(formBody)
                .header("Authorization",this.authorization)
                .build();
        Response execute = okHttpClient.newCall(request).execute();
        System.out.println(execute.body().string());
    }

    public JSONArray homeWorkList(OkHttpClient okHttpClient) throws IOException {
        FormBody formBody = new FormBody.Builder()
                .add("recruitId",this.recruitId)
                .add("courseId",this.courseId)
                .add("flag","1")
                .add("pageSize","100")
                .add("pageNum","0")
                .add("uuid",this.uuid).build();
        final Request request = new Request.Builder()
                .url("https://studentexam.zhihuishu.com/studentExam/student/getStudentHomework")
                .addHeader("Authorization",this.authorization)
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject object = JSONObject.parseObject(response.body().string());
        return object.getJSONObject("rt").getJSONArray("studentHomeworkList");
    }
    private String getAuthorization(OkHttpClient okHttpClient) throws IOException {
        final Request request1 = new Request.Builder()
                .url("https://studentexam.zhihuishu.com/studentExam/student/getExamToken?recruitId="+this.recruitId+"&uuid="+this.uuid)
                .header("ev",Encrypt.encrypt(uuid+";"+recruitId))
                .get()
                .build();
        Response response1 = okHttpClient.newCall(request1).execute();
        return JSON.parseObject(response1.body().string()).getString("rt");
    }

    public void generateAnswer(OkHttpClient okHttpClient,String stuExamId,String examId,String questionIds) throws IOException {
        FormBody formBody = new FormBody.Builder()
                .add("recruitId",this.recruitId)
                .add("examId",examId)
                .add("stuExamId",stuExamId)
                .add("schoolId",this.schoolId)
                .add("courseId",this.courseId)
                .add("questionIds",questionIds)
                .add("uuid",this.uuid)
                .build();
        final Request request = new Request.Builder()
                .url("https://studentexam.zhihuishu.com/studentExam/answer/getStuAnswerInfoNew")
                .post(formBody)
                .header("Authorization",this.authorization)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject questions = ((JSONObject) JSONObject.parse(response.body().string())).getJSONObject("rt");
        File file = new File("src/main/resources/"+examId);
        file.deleteOnExit();
        FileWriter writer = new FileWriter(file);
        writer.write(questions.toJSONString());
        writer.flush();
    }

    public void setAuthorization(OkHttpClient okHttpClient) throws IOException {
        this.authorization = getAuthorization(okHttpClient);
    }
}
