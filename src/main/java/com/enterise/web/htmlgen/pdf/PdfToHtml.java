/*package com.enterise.web.htmlgen.pdf;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.text.PDFTextStripper;

import com.enterise.web.htmlgen.HtmlGenerator;
import com.hailian.utils.FileUtil;
*//**
 * 
 * @time   2017年12月29日 上午11:23:48
 * @author zuoqb
 * @todo   pdf转html
 *//*
public class PdfToHtml implements HtmlGenerator {

	private PDDocument document;
	PdfHtmlBuilder pdfHtmlBuilder = new PdfHtmlBuilder();
	private List pages;
	private String fileName;
	private String filePath;
	
	public PdfToHtml(String filePath,String fileName) {
		InputStream is=null;
		try {
			is = new FileInputStream(filePath+fileName);
			document = PDDocument.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.fileName=fileName;
		this.filePath=filePath;
	}
	
	public String generate() throws Exception{
		
		int numberOfPages = document.getNumberOfPages();
		//List pages = document.getDocumentCatalog().getAllPages();
		
		PDFTextStripper pdfTextStripper = null;
		pdfTextStripper = new PDFTextStripper();
		for (int i = 1; i <= numberOfPages; i++) {
			
			pdfHtmlBuilder.addHtmlPage(new PdfHtmlPage());
			PdfHtmlPage page = (PdfHtmlPage)pdfHtmlBuilder.getCurrentPage();
						
			pdfTextStripper.setStartPage(i);
			pdfTextStripper.setEndPage(i);
			String text = null;
			try {
				text = pdfTextStripper.getText(document);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Reader reader = new StringReader(text);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String textLine = null;
			while ((textLine = bufferedReader.readLine()) != null) {
				page.addTextLine(textLine);
			}
			InputStreamReader bufferedReader = new InputStreamReader(
	                new FileInputStream(text),"utf-8");//考虑到编码格式
			 BufferedReader reader = new BufferedReader(bufferedReader);
			String textLine = null;
			while ((textLine = reader.readLine()) != null) {
				page.addTextLine(textLine);
			}
			PDPage pDPage = (PDPage)pages.get(i - 1);
			PDResources resources = pDPage.getResources();
			Map images;
			images = resources.getImages();
			if( images != null ){
                Iterator imageIter = images.keySet().iterator();
                while( imageIter.hasNext() )
                {
                    String key = (String)imageIter.next();
                    PDXObjectImage image = (PDXObjectImage)images.get( key );
                    //page.addPicture(image, image.getSuffix());
                    page.addPicture(image, "png");
                }
            }
		}
		document.close();
		
		return pdfHtmlBuilder.writeToFile(filePath,fileName);
	}
	
}*/