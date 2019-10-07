import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:QRCodeTest
 * Package:PACKAGE_NAME
 * Description:TODO
 *
 * @date:2019/9/9 10:46
 * @author:guoxin
 */
public class QRCodeTest {

    public static void main(String[] args) throws WriterException, IOException {

        Map<EncodeHintType,Object> encodeHintTypeObjectMap = new HashMap<EncodeHintType, Object>();
        encodeHintTypeObjectMap.put(EncodeHintType.CHARACTER_SET,"UTF-8");

        //生成一个矩阵对象
        BitMatrix bitMatrix = new MultiFormatWriter().encode("weixin://wxpay/bizpayurl?pr=Mxz8RTl", BarcodeFormat.QR_CODE,200,200,encodeHintTypeObjectMap);

        String filePath = "D://";
        String fileName = "qrcode.png";

        Path path = FileSystems.getDefault().getPath(filePath,fileName);

        //将矩阵对象转换为二维码图片
        MatrixToImageWriter.writeToPath(bitMatrix,"png",path);

        System.out.println("生成图片");


    }
}
