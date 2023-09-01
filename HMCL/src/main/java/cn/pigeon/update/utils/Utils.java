package cn.pigeon.update.utils;

import cn.pigeon.update.Static;
import cn.pigeon.update.data.Token;
import okhttp3.HttpUrl;
import org.jackhuang.hmcl.auth.Account;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.jackhuang.hmcl.setting.ConfigHolder.config;

public class Utils {
    public static String getMacAddress() {
        try {
            InetAddress inetAddress = null;
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                for (Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses(); inetAddresses.hasMoreElements(); ) {
                    InetAddress inetAddr = inetAddresses.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            inetAddress = inetAddr;
                        }
                        if (inetAddress == null) {
                            inetAddress = inetAddr;
                        }
                    }
                }
            }
            if (inetAddress == null) {
                inetAddress = InetAddress.getLocalHost();
            }
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);
            byte[] mac = networkInterface.getHardwareAddress();
            String[] temp = new String[mac.length];
            for (int i = 0; i < mac.length; i++) {
                temp[i] = Integer.toHexString(mac[i] & 0xff);
                if (temp[i].length() == 1) {
                    temp[i] = "0" + temp[i];
                }
            }
            String result = String.join("-", temp);
            return result.toUpperCase();
        } catch (SocketException | UnknownHostException e) {
            Static.logger.log(Level.INFO, e.getMessage());
        }
        return Static.defaultMacAddress;
    }

    public static String calculateMD5(File path) throws NoSuchAlgorithmException {
        if (!path.exists() || !path.isFile()) {
            return "0";
        }
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (DigestInputStream dis = new DigestInputStream(Files.newInputStream(path.toPath()), md)) {
            byte[] buffer = new byte[8192];
            while (dis.read(buffer) != -1) ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] mdBytes = md.digest();
        return DatatypeConverter.printHexBinary(mdBytes).toLowerCase();
    }

    public static String calculateMD5(String string) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] mdBytes = md.digest(string.getBytes(StandardCharsets.UTF_8));
        return DatatypeConverter.printHexBinary(mdBytes).toLowerCase();
    }

    public static void unzip(File zipFile, Path destPath) throws IOException {
        try (ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(zipFile.toPath()))) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                File filePath = destPath.resolve(entry.getName()).toFile();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath);
                } else {
                    filePath.mkdir();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }

    private static void extractFile(ZipInputStream zipIn, File filePath) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = zipIn.read(buffer)) != -1) {
                fileOut.write(buffer, 0, bytesRead);
            }
        }
    }

    public static void listFiles(Path basePath, File file, ArrayList<String> fileArrayList) {
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            if (fileList != null) {
                for (File subFile : fileList) {
                    listFiles(basePath, subFile, fileArrayList);
                }
            }
        } else {
            fileArrayList.add(basePath.relativize(file.toPath()).toString());
        }
    }

    public static void listFiles(File file, ArrayList<File> fileArrayList) {
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            if (fileList != null) {
                for (File subFile : fileList) {
                    listFiles(subFile, fileArrayList);
                }
            }
        } else {
            fileArrayList.add(file);
        }
    }

    public static String getBaseUrl(Account account, Token token, String packName) {
        final String username = account.getUsername();
        final String uuid = account.getUUID().toString().replace("-", "");
        final String accessToken = token.key;
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(config().getBaseUrl())).newBuilder();
        builder.addPathSegment("api");
        builder.addPathSegment("get-source");
        builder.addQueryParameter("macAddress", getMacAddress());
        builder.addQueryParameter("username", username);
        builder.addQueryParameter("uuid", uuid);
        builder.addQueryParameter("accessKey", accessToken);
        builder.addQueryParameter("packName", packName);
        return builder.build().toString();
    }

    public static Map<URL, String> prepareForDownload(String baseUrl, ArrayList<String> fileArrayList) {
        Map<URL, String> urls = new HashMap<>();
        for (String file : fileArrayList) {
            HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(baseUrl)).newBuilder();
            builder.addPathSegment(file);
            urls.put(builder.build().url(), file);
        }
        return urls;
    }
}
