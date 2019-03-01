package com.enterise.web.htmlgen.ppt;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public class Ppt2PdfUtils{
	public static void main(String[] args){
		try {

			String inPath="C://Users//Administrator//Desktop//资料//触点日清分析_20180523.pptx";
			String outPath = "C://Users//Administrator//Desktop//资料//触点日清分析_20180523.pdf";
			String type="pptx";
			ppt2Pdf(inPath,outPath,type);
		} catch (Exception e) {
			// handling of wrong arguments
			System.err.println(e.getMessage());
		}


	}

	public static boolean ppt2Pdf(String inPath,String outPath,String type)  {
		try {
			boolean shouldShowMessages = true;
			if(outPath == null){
				outPath = changeExtensionToPDF(inPath);
			}
			
			InputStream inStream = getInFileStream(inPath);
			OutputStream outStream = getOutFileStream(outPath);
			
			Converter converter;
			switch(type){
			case "ppt":  converter = new PptToPDFConverter(inStream, outStream, shouldShowMessages, true);
			break;
			case "pptx": converter = new PptxToPDFConverter(inStream, outStream, shouldShowMessages, true);
			break;
			case "odt": converter = new OdtToPDF(inStream, outStream, shouldShowMessages, true);
			break;
			default: converter = null;
			break;

			}
			converter.convert();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	//From http://stackoverflow.com/questions/941272/how-do-i-trim-a-file-extension-from-a-string-in-java
	public static String changeExtensionToPDF(String originalPath) {
		String filename = originalPath;
		int extensionIndex = filename.lastIndexOf(".");

		String removedExtension;
		if (extensionIndex == -1){
			removedExtension =  filename;
		} else {
			removedExtension =  filename.substring(0, extensionIndex);
		}
		String addPDFExtension = removedExtension + ".pdf";
		return addPDFExtension;
	}
	
	
	protected static InputStream getInFileStream(String inputFilePath) throws FileNotFoundException{
		File inFile = new File(inputFilePath);
		FileInputStream iStream = new FileInputStream(inFile);
		return iStream;
	}
	
	protected static OutputStream getOutFileStream(String outputFilePath) throws IOException{
		File outFile = new File(outputFilePath);
		
		try{
			//Make all directories up to specified
			outFile.getParentFile().mkdirs();
		} catch (NullPointerException e){
			//Ignore error since it means not parent directories
		}
		
		outFile.createNewFile();
		FileOutputStream oStream = new FileOutputStream(outFile);
		return oStream;
	}











}
