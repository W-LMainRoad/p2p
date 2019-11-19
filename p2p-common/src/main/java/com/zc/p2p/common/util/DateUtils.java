package com.zc.p2p.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ClassName:DateUtils
 * Package:com.zc.p2p.common.util
 * Description:
 *
 * @date:2019/11/12 14:14
 * @author:youxiang@163.com
 */
public class DateUtils {
    /**
     * 获取时间戳
     * @return
     */
    public static String getTimeStamp() {

        return new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date());
    }
}
