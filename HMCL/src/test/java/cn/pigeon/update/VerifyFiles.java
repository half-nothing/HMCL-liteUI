package cn.pigeon.update;

import cn.pigeon.update.enums.SyncMode;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class VerifyFiles {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        File target = new File("D:\\WorkSpace\\Java\\HMCL\\.minecraft\\mods");
        File targetFIle = new File("D:\\WorkSpace\\Java\\HMCL\\.minecraft\\pigeon\\mods.json");
        cn.pigeon.update.tasks.VerifyFiles verifyFiles = new cn.pigeon.update.tasks.VerifyFiles(target.toPath(), targetFIle, SyncMode.FORCE);
        ArrayList<String> fileArrayList = verifyFiles.verifyFile();
        for (String file :
                fileArrayList) {
            System.out.println(file);
        }
//        ArrayList<String> arrayList = new ArrayList<>();
//        Path basePath = Paths.get("D:\\WorkSpace\\web\\Server-Backend-Interface\\source\\test");
//        listFiles(basePath, new File("D:\\WorkSpace\\web\\Server-Backend-Interface\\source\\test\\mods"), arrayList);
//        for (String file :
//                arrayList) {
//            System.out.println(file);
//        }
    }
//    public static void listFiles(Path basePath, File file, ArrayList<String> fileArrayList) {
//        if (file.isDirectory()) {
//            File[] fileList = file.listFiles();
//            if (fileList != null) {
//                for (File subFile : fileList) {
//                    listFiles(basePath, subFile, fileArrayList);
//                }
//            }
//        } else {
//            fileArrayList.add(basePath.relativize(file.toPath()).toString());
//        }
//    }
}
