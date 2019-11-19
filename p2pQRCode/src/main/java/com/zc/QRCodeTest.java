package com.zc;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:QRCodeTest
 * Package:com.zc
 * Description:
 *
 * @date:2019/11/13 10:11
 * @author:youxiang@163.com
 */
public class QRCodeTest {
    @Test
    public void generateQRCode() throws WriterException, IOException {
        Map<EncodeHintType,Object> hintMap = new HashMap<EncodeHintType, Object>();
        hintMap.put(EncodeHintType.CHARACTER_SET,"UTF-8");
        //创建一个矩阵对象
        BitMatrix bitMatrix = new MultiFormatWriter().encode("baidu", BarcodeFormat.QR_CODE,200,200,hintMap);

        String filePath = "D://";
        String fileName = "qrCode.jpg";

        Path path = FileSystems.getDefault().getPath(filePath,fileName);

        //将矩阵对象转换为图片
        MatrixToImageWriter.writeToPath(bitMatrix,"jpg",path);

    }
}
