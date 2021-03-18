package it.polimi.db2.db2_project.web.utils;

import java.util.Base64;

public class ImageUtil {
    public String getImgData(byte[] byteData) {
        return Base64.getMimeEncoder().encodeToString(byteData);
    }
}