import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.*;
import java.util.*;

/**
 * @Classname AppRun
 * @Description TODO
 * @Date 2021/10/9 22:18
 * @Created by 晨曦
 */
public class AppRun {
    public static void main(String[] args) throws IOException {
        Login login = new Login("200401201","Gcx200401230","632",false);
        LessonAction lessonAction = new LessonAction(
                login.uuid, "1633813007000",
                "42515b5845524258454a585859465d42");
        //大学生心理健康教育 435159514d524258454a585f5b4d5b40
        //传统文化 425a515040524258454a58595e445a4a
        //sql 42515b5845524258454a585859465d42
        login.login(lessonAction);
        JSONArray objects = lessonAction.videoList(login.okHttpClient);
        for (Object o:objects){
            JSONObject chapter = (JSONObject) o;
            for (Object c : chapter.getJSONArray("videoLessons")){
                JSONObject le = (JSONObject) c;
                lessonAction.preExe(login.okHttpClient,chapter.getInteger("id").toString(),le.getInteger("id").toString(),le.getInteger("videoId").toString());
                lessonAction.goExe(login.okHttpClient,"", LessonAction.evCreator(new String[]{lessonAction.recruitId, le.getInteger("id").toString(), "0", le.getInteger("videoId").toString(), chapter.getInteger("id").toString(), "0", le.getInteger("videoSec").toString(), le.getInteger("videoSec").toString(), "01:59:41"}), "NTQ2MTAyNTE0Mg==", lessonAction.courseId);
            }
        }


        //刷题
        ExamAction examAction = new ExamAction(lessonAction.recruitId,lessonAction.courseId,login.uuid,login.schoolId);
        login.login(examAction);
        examAction.setAuthorization(login.okHttpClient);
        JSONArray objects1 = examAction.homeWorkList(login.okHttpClient);
        Map<String,File> fileMap = new HashMap<>();
        for(File file:new File("src/main/resources/").listFiles()){
            fileMap.put(file.getName(),file);
        }
        for (Object o :objects1){
            JSONObject studentHomework = (JSONObject) o;
            File file = fileMap.get(studentHomework.getString("examId"));
            if (file==null){
                continue;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            JSONObject jsonObject = JSONObject.parseObject(reader.readLine());
            for (String key:jsonObject.keySet()){
                JSONObject answer = jsonObject.getJSONObject(key);
                examAction.sendAnswer(login.okHttpClient,studentHomework.getString("id"),studentHomework.getString("examId"),key,answer.getString("answer").contains(",")?answer.getString("answer"):Integer.valueOf(answer.getString("answer")),null);
            }
            JSONArray jsonArray = examAction.contentList(login.okHttpClient, studentHomework.getString("id"), studentHomework.getString("examId"));
            for (Object object:jsonArray){
                JSONObject j = (JSONObject) object;
                JSONArray questionDtos = j.getJSONArray("questionDtos");
                for (Object obj:questionDtos){
                    JSONObject question = (JSONObject) obj;
                }
            }
        }
    }
}