package qrcodeapi.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

@Service
public class BaseService {
    Map<String, MediaType> types = Map.of(
            "PNG", MediaType.IMAGE_PNG,
            "JPEG", MediaType.IMAGE_JPEG,
            "GIF", MediaType.IMAGE_GIF
    );

    Map<String, ErrorCorrectionLevel> corrections = Map.of(
            "L", ErrorCorrectionLevel.L,
            "M", ErrorCorrectionLevel.M,
            "Q", ErrorCorrectionLevel.Q,
            "H", ErrorCorrectionLevel.H
    );

    public ResponseEntity<?> getQRCode(String contents, int size, String type, String correction) {

        if (contents.isEmpty() || contents.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Contents cannot be null or blank"));
        }

        if (size < 150 || size > 350) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Image size must be between 150 and 350 pixels"));
        }

        if (!corrections.containsKey(correction.toUpperCase())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error","Permitted error correction levels are L, M, Q, H"));
        }

        if (!types.containsKey(type.toUpperCase())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Only png, jpeg and gif image types are supported"));
        }

        BufferedImage bufferedImage = encodeQRCode(contents, size, type, correction);

        return ResponseEntity.ok()
                .contentType(types.get(type.toUpperCase()))
                .body(bufferedImage);
    }

    private BufferedImage encodeQRCode(String contents, int size, String type, String correction) {
        QRCodeWriter writer = new QRCodeWriter();
        BufferedImage bufferedImage = null;
        Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, corrections.get(correction));
        try {
            BitMatrix bitMatrix = writer.encode(contents, BarcodeFormat.QR_CODE, size, size, hints);
            bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            System.out.println("Couldn't write image.");
        }
        return bufferedImage;
    }
}
