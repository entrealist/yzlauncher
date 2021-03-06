package com.gofun.zxing.encode;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author lilin
 * @time on 2020/8/26 下午5:06
 */
public class QrCodeGenWrapper {

    private static final int QUIET_ZONE_SIZE = 4;

    public static QrCodeConfig.QrCodeConfigBuilder createQrCodeConfig() {
        return new QrCodeConfig.QrCodeConfigBuilder();
    }


    /*public static BufferedImage asBufferedImage(QrCodeConfig qrCodeConfig) throws WriterException, IOException {
        BitMatrix bitMatrix = encode(qrCodeConfig);
        return MatrixToImageUtil.toBufferedImage(qrCodeConfig, bitMatrix);
    }*/



    public static boolean asFile(QrCodeConfig qrCodeConfig, String absFileName) throws WriterException, IOException {
        File file = createFile(absFileName);
        if (file == null) {
            throw new IllegalArgumentException("file not exists! absFile: " + absFileName);
        }

        //BufferedImage bufferedImage = asBufferedImage(qrCodeConfig);
        /*if (!ImageIO.write(bufferedImage, qrCodeConfig.getPicType(), file)) {
            throw new IOException("save qrcode image error!");
        }*/
        return true;
    }


    /**
     * 对 zxing 的 QRCodeWriter 进行扩展, 解决白边过多的问题
     * <p/>
     * 源码参考
     */
    private static BitMatrix encode(QrCodeConfig qrCodeConfig) throws WriterException {
        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
        int quietZone = 1;
        if (qrCodeConfig.hints != null) {
            if (qrCodeConfig.hints.containsKey(EncodeHintType.ERROR_CORRECTION)) {
                errorCorrectionLevel = ErrorCorrectionLevel.valueOf(qrCodeConfig.hints.get(EncodeHintType.ERROR_CORRECTION).toString());
            }
            if (qrCodeConfig.hints.containsKey(EncodeHintType.MARGIN)) {
                quietZone = Integer.parseInt(qrCodeConfig.hints.get(EncodeHintType.MARGIN).toString());
            }

            if (quietZone > QUIET_ZONE_SIZE) {
                quietZone = QUIET_ZONE_SIZE;
            } else if (quietZone < 0) {
                quietZone = 0;
            }
        }

        QRCode code = Encoder.encode(qrCodeConfig.msg, errorCorrectionLevel, qrCodeConfig.hints);
        return renderResult(code, qrCodeConfig.w, qrCodeConfig.h, quietZone);
    }


    /**
     * 对 zxing 的 QRCodeWriter 进行扩展, 解决白边过多的问题
     * <p/>
     * 源码参考
     *
     * @param code
     * @param width
     * @param height
     * @param quietZone 取值 [0, 4]
     * @return
     */
    private static BitMatrix renderResult(QRCode code, int width, int height, int quietZone) {
        ByteMatrix input = code.getMatrix();
        if (input == null) {
            throw new IllegalStateException();
        }

        // xxx 二维码宽高相等, 即 qrWidth == qrHeight
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        int qrWidth = inputWidth + (quietZone * 2);
        int qrHeight = inputHeight + (quietZone * 2);


        // 白边过多时, 缩放
        int minSize = Math.min(width, height);
        int scale = calculateScale(qrWidth, minSize);
        if (scale > 0) {
            int padding, tmpValue;
            // 计算边框留白
            padding = (minSize - qrWidth * scale) / QUIET_ZONE_SIZE * quietZone;
            tmpValue = qrWidth * scale + padding;
            if (width == height) {
                width = tmpValue;
                height = tmpValue;
            } else if (width > height) {
                width = width * tmpValue / height;
                height = tmpValue;
            } else {
                height = height * tmpValue / width;
                width = tmpValue;
            }
        }

        int outputWidth = Math.max(width, qrWidth);
        int outputHeight = Math.max(height, qrHeight);

        int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
        int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
        int topPadding = (outputHeight - (inputHeight * multiple)) / 2;

        BitMatrix output = new BitMatrix(outputWidth, outputHeight);

        for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
            // Write the contents of this row of the barcode
            for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
                if (input.get(inputX, inputY) == 1) {
                    output.setRegion(outputX, outputY, multiple, multiple);
                }
            }
        }

        return output;
    }


    /**
     * 如果留白超过15% , 则需要缩放
     * (15% 可以根据实际需要进行修改)
     *
     * @param qrCodeSize 二维码大小
     * @param expectSize 期望输出大小
     * @return 返回缩放比例, <= 0 则表示不缩放, 否则指定缩放参数
     */
    private static int calculateScale(int qrCodeSize, int expectSize) {
        if (qrCodeSize >= expectSize) {
            return 0;
        }

        int scale = expectSize / qrCodeSize;
        int abs = expectSize - scale * qrCodeSize;
        if (abs < expectSize * 0.15) {
            return 0;
        }

        return scale;
    }

    /**
     * 创建文件
     *
     * @param filename
     * @return
     */
    public static File createFile(String filename) throws FileNotFoundException {
        if (filename == null || "".equals(filename)) {
            return null;
        }

        int index = filename.lastIndexOf('/');
        if (index <= 0) {
            return new File(filename);
        }

        String path = filename.substring(0, index);
        mkDir(new File(path));
        return new File(filename);
    }

    /**
     * 递归生成目录, file 为根据相对路径创建时, 会抛npe
     * @param file
     * @throws FileNotFoundException
     */
    public static void mkDir(File file) throws FileNotFoundException {
        if (file.getParentFile() == null) {
            file = file.getAbsoluteFile();
        }

        if (file.getParentFile().exists()) {
            if (!file.exists() && !file.mkdir()) {
                throw new FileNotFoundException();
            }
        } else {
            mkDir(file.getParentFile());
            if (!file.exists() && !file.mkdir()) {
                throw new FileNotFoundException();
            }
        }
    }
}
