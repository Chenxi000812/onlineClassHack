import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

/**
 * @Classname GenAppRun
 * @Description TODO
 * @Date 2021/11/27 17:35
 * @Created by 晨曦
 */
public class GenAppRun {
        public static void main(String[] args) throws IOException {
            Login login = new Login("19916090626","Wcx929326179","632",true);
            LessonAction lessonAction = new LessonAction(login.uuid, "1633813007000","42515b5845524258454a585859465d42");
            login.login(lessonAction);
            JSONArray objects = lessonAction.videoList(login.okHttpClient);
            ExamAction examAction = new ExamAction(lessonAction.recruitId,lessonAction.courseId,login.uuid,login.schoolId);
            login.login(examAction);
            examAction.setAuthorization(login.okHttpClient);
            JSONArray objects1 = examAction.homeWorkList(login.okHttpClient);
            System.out.println(objects1.toJSONString());
            for (Object o :objects1){
                JSONObject studentHomework = (JSONObject) o;
                JSONArray jsonArray = examAction.contentList(login.okHttpClient, studentHomework.getString("id"), studentHomework.getString("examId"));
                StringBuilder sb = new StringBuilder();
                for (Object object:jsonArray){
                    JSONObject j = (JSONObject) object;
                    JSONArray questionDtos = j.getJSONArray("questionDtos");
                    for (Object obj:questionDtos){
                        JSONObject question = (JSONObject) obj;
                        sb.append(question.getString("eid"));
                        sb.append(",");
                    }
                }
                sb.deleteCharAt(sb.length()-1);
                examAction.generateAnswer(login.okHttpClient,studentHomework.getString("id"),studentHomework.getString("examId"),sb.toString());
            }

    }
}
