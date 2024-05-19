package com.devhp.SpringRestDemoWithGradle.util.AppUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import org.imgscalr.Scalr;

public class AppUtil {
    public static String getPhotoUploadPath(String fileName,String folderName, long albumId) throws IOException {
        String path = "src\\main\\resources\\static\\uploads\\"+albumId+"\\"+folderName;
        Files.createDirectories(Paths.get(path));
        return new File(path).getAbsolutePath() + "\\" + fileName;
    }

    public static BufferedImage getThumbnail(MultipartFile originalFile, Integer width) throws IOException{
        BufferedImage thumbImg = null;
        BufferedImage img = ImageIO.read(originalFile.getInputStream());
        thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, width, Scalr.OP_ANTIALIAS);
        return thumbImg;
    }

    public static Resource getFileAsResource(long albumId, String folderName, String fileName) throws IOException {
        String location = "src\\main\\resources\\static\\uploads\\" + albumId + "\\" + folderName + "\\" + fileName;
        File file = new File(location);
        if(file.exists()){
            Path path = Paths.get(file.getAbsolutePath());
            return new UrlResource(path.toUri());

        }else {
            return null;
        }
    }
    
}
