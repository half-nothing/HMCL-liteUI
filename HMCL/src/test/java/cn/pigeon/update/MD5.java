package cn.pigeon.update;

import cn.pigeon.update.utils.Utils;

import java.io.File;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(Utils.calculateMD5(new File("C:\\Users\\Administrator\\Desktop\\AbyssalCraft Integration-1.12.2-1.11.3.jar")));
        System.out.println(Utils.calculateMD5("GetMacAddressTest.java"));
    }
}
