package com.tyr.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

public class CommonUtil
{
	public static Image loadImage(String path) {  
		String pathName = new File(".").getAbsolutePath()+"/imgs/"+path;
		Image image = null;
        // 该方法不推荐使用，该方法是懒加载，但加载的更快，图像并不加载到内存，当拿图像的宽和高时会返回-1；  
        image = Toolkit.getDefaultToolkit().getImage(pathName);  
        /*try {  
            // 该方法会将图像加载到内存，从而拿到图像的详细信息。  
            image = ImageIO.read(new FileInputStream(pathName));  
        } catch (Exception e) {  
            e.printStackTrace();  
        } */
        return image;
    }  
}
