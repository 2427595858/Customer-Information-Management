package sqlservertest;

public class test1 {
    public static void main(String[] args) {
        String str = "123&pageNum=1";
        if (str.contains("&pageNum=")) {
            int index = str.lastIndexOf("&pageNum");
            str = str.substring(0, index);
            str = "001"+"220"+"?"+str;
            System.out.println(str);
        }
    }
}
