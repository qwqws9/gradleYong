package yong.common;

import java.text.SimpleDateFormat;

public class DateFormat {

    /**
     * 
     * yyyyMMddHHmmss 현재시간 리턴
     *
     * @since 2020. 3. 17.
     * @author yong
     *
     * @return
     */
    public static String getRegDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(System.currentTimeMillis());
    }
}
