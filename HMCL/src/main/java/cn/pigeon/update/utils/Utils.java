package cn.pigeon.update.utils;

import cn.pigeon.update.Static;
import cn.pigeon.update.data.Token;
import okhttp3.HttpUrl;
import org.jackhuang.hmcl.auth.yggdrasil.YggdrasilAccount;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
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

import static cn.pigeon.update.Static.logger;
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

    public static String calculateMD5(File path) {
        Objects.requireNonNull(path);
        if (!path.exists() || !path.isFile()) {
            return "0";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try (DigestInputStream dis = new DigestInputStream(Files.newInputStream(path.toPath()), md)) {
                byte[] buffer = new byte[8192];
                while (dis.read(buffer) != -1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byte[] mdBytes = md.digest();
            return DatatypeConverter.printHexBinary(mdBytes).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
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

    public static HttpUrl.Builder getBaseUrl(YggdrasilAccount account, Token token, String packName) {
        final String username = account.getCharacter();
        final String uuid = account.getUUID().toString().replace("-", "");
        final String accessToken = token.data;
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(config().getBaseUrl())).newBuilder();
        builder.addPathSegment("api");
        builder.addPathSegment("launcher");
        builder.addPathSegment("get-source");
        builder.addQueryParameter("macAddress", getMacAddress());
        builder.addQueryParameter("username", username);
        builder.addQueryParameter("uuid", uuid);
        builder.addQueryParameter("accessKey", accessToken);
        builder.addQueryParameter("packName", packName);
        return builder;
    }

    public static Map<URL, File> prepareForDownload(HttpUrl.Builder urlBuilder, Map<File, String> fileArrayList) {
        Map<URL, File> urls = new HashMap<>();
        for (Map.Entry<File, String> entry : fileArrayList.entrySet()) {
            urlBuilder.addEncodedPathSegment(entry.getValue().replace('\\', '/'));
            urls.put(urlBuilder.build().url(), entry.getKey());
            urlBuilder.removePathSegment(urlBuilder.getEncodedPathSegments$okhttp().size() - 1);
        }
        return urls;
    }

    public static String readJsonStringFromFile(File jsonFile) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(jsonFile.toPath()), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return stringBuilder.toString();
    }
}