package com.myunihome.myxapp.web.test;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;



public class QRCodeEvents {
	
	public static void main(String []args)throws Exception{
		String text = "你好";
		int width = 200;
		int height = 200;
		String format = "png";
		Map<EncodeHintType, Object> hints= new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 1);
		 BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height,hints);
		 File outputFile = new File("new4.png");
		 //MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
		 Path filepath=outputFile.toPath();
		 MatrixToImageWriter.writeToPath(bitMatrix, format, filepath);
		 
	}
}
