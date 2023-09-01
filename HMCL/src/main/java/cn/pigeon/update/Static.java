package cn.pigeon.update;

import okhttp3.OkHttpClient;

import java.util.logging.Logger;

public class Static {
    public final static OkHttpClient okHttpClient = new OkHttpClient();
    public final static String defaultMacAddress = "00-00-00-00-00-00";
    public final static Logger logger = Logger.getLogger("HMCL");
    public static int updateMaxThread = 8;
}
