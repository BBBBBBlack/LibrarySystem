package com.bbbbbblack.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
@Component
public class ThumbnailsUtil {
    @Value("${file.real-path}")
    String realPath;
    public void thumbnails(OutputStream os,String fileName) throws IOException {
        String filePath= realPath+fileName;
        Thumbnails.of(filePath)
                .scale(0.25f)
                .toOutputStream(os);
    }
}
