package com.myunihome.myxapp.web.test;

import java.io.File;

import com.myunihome.myxapp.paas.qrcode.factory.QRCodeFactory;



public class QRCodeTest {
	
	public static void main(String []args)throws Exception{
		String text = "你好";
		int width = 200;
		int height = 200;
		/*String format = "png";
		Map<EncodeHintType, Object> hints= new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 1);
		 BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height,hints);
		 
		 //MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
		 Path filepath=outputFile.toPath();
		 MatrixToImageWriter.writeToPath(bitMatrix, format, filepath);*/
		File outputFile = new File("sdk-qrcode1.png");
		 QRCodeFactory.getZxingQRCodeClient().writeQRCode(outputFile, text, width, height);
		 
	}
}
