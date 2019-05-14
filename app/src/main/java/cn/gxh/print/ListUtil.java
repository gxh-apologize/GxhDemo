package cn.gxh.print;
import java.util.List;

public class ListUtil {

    public static boolean isEmpty(List list) {
        return (list == null || list.isEmpty()) ? true : false;
    }
}
