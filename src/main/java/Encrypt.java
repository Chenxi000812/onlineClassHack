/**
 * @Classname Encrypt
 * @Description TODO
 * @Date 2021/10/10 2:12
 * @Created by 晨曦
 */
public class Encrypt {
    public static String encrypt(String content) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < content.length(); i++) {
            String a = Integer.toHexString(content.codePointAt(i) ^ "zzpttjd".codePointAt(i%"zzpttjd".length()));
            a = a.length()<2? "0"+a:a;
            sb.append(a.length()>4?a.substring(0,4):a);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(encrypt("89200;1000343513;0;150851;1000117812;0;0;223;00:02:34"));
        System.out.println(encrypt("XoQJDYP4;82885"));
    }
}
//424342444451554a4a40474059514b494b444f5b514a4245454f5b544a4a41454352554841404f44515648494b4444505448404340
//424342444451554a4a40474059514b494b444f5b514a4245454f5b544a4a41454352554841404f44515648494b4444505448404340