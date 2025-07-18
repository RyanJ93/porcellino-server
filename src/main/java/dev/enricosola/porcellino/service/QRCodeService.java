package dev.enricosola.porcellino.service;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.WriterException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.BarcodeFormat;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Base64;
import java.awt.*;

@Service
public class QRCodeService {
    /**
     * Generates a QR code as a BufferedImage based on the provided content and size.
     *
     * @param content the text content to encode into the QR code
     * @param size the width and height of the QR code in pixels
     * @return a BufferedImage representing the generated QR code
     * @throws WriterException if an error occurs during the QR code generation
     */
    public BufferedImage generateAsImage(String content, int size) throws WriterException {
        BitMatrix bitMatrix = this.generateQRCode(content, size);
        return this.renderQRCodeAsImage(bitMatrix);
    }

    /**
     * Generates a QR code as a Base64-encoded PNG image string based on the provided content and size.
     *
     * @param content the text content to encode into the QR code
     * @param size the width and height of the QR code in pixels
     * @return a Base64-encoded string representing the QR code as a PNG image
     * @throws WriterException if an error occurs during the QR code generation
     * @throws IOException if an error occurs while writing the image data
     */
    public String generateAsBase64(String content, int size) throws WriterException, IOException {
        BufferedImage bufferedImage = this.generateAsImage(content, size);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }

    /**
     * Generates a QR code as a BitMatrix based on the provided content and size.
     *
     * @param content the text content to encode into the QR code
     * @param size the width and height of the QR code in pixels
     * @return a BitMatrix representing the generated QR code
     * @throws WriterException if an error occurs during the QR code generation
     */
    private BitMatrix generateQRCode(String content, int size) throws WriterException {
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        return qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, size, size, hintMap);
    }

    /**
     * Renders a QR code represented by a BitMatrix as a BufferedImage.
     *
     * @param bitMatrix the BitMatrix object representing the QR code structure
     * @return a BufferedImage containing the rendered QR code
     */
    private BufferedImage renderQRCodeAsImage(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth(), height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (bitMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        return image;
    }
}
