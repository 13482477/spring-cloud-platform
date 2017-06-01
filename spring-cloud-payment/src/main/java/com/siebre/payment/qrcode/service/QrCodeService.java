package com.siebre.payment.qrcode.service;

import java.io.OutputStream;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.siebre.payment.qrcode.utils.MatrixToImageWriter;

@Component
public class QrCodeService {
	
	public void generateQrCode(String text, OutputStream outputStream) {
		try {
			int qrcodeWidth = 300;
			int qrcodeHeight = 300;
			String qrcodeFormat = "png";
			HashMap<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, qrcodeWidth, qrcodeHeight, hints);
			
			MatrixToImageWriter.writeToStream(bitMatrix, qrcodeFormat, outputStream);
		} catch (Exception e) {
			throw new RuntimeException("Generate QRCode error!");
		}
	}

}
