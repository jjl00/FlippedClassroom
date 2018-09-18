package io.flippedclassroom.server.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {
    /**
     * 图片格式转换
     */
    public static boolean convertFormat(String inputImagePosition, String outputImagePosition, String format) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(inputImagePosition);
        FileOutputStream fileOutputStream = new FileOutputStream(outputImagePosition);

        BufferedImage bufferedImage = ImageIO.read(fileInputStream);
        boolean result = ImageIO.write(bufferedImage, format, fileOutputStream);

        fileInputStream.close();
        fileOutputStream.close();

        return result;
    }
}
