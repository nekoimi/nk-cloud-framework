package com.nekoimi.nk.framework.base.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * nekoimi  2021/12/14 9:10
 */
public class ErrorUtils {

    /**
     * @param e
     * @return
     */
    public static String getStackTrace(Throwable e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
