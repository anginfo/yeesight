package com.enterise.web.htmlgen.pdf;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.core.io.ClassPathResource;

import com.hailian.utils.FileUtil;

public class PdfToImage {
	public static void main(String[] args) {
		// pdfToImage("D://kubi//backups//20181016//暗访样本 -CN.pdf");
	}

	// 经过测试,dpi为96,100,105,120,150,200中,105显示效果较为清晰,体积稳定,dpi越高图片体积越大
	// 一般电脑显示分辨率为96
	public static final float DEFAULT_DPI = 105;

	/**
	 * pdf转图片
	 * 
	 * @param pdfPath
	 */
	public static String pdfToImage(String filePath, String fileName,
			String orgName) {
		try {
			// 图像合并使用参数
			int width = 0; // 总宽度
			int[] singleImgRGB; // 保存一张图片中的RGB数据
			int shiftHeight = 0;
			BufferedImage imageResult = null;// 保存每张图片的像素值
			// 利用PdfBox生成图像
			PDDocument pdDocument = PDDocument.load(new File(filePath
					+ fileName));
			PDFRenderer renderer = new PDFRenderer(pdDocument);
			// 循环每个页码
			for (int i = 0, len = pdDocument.getNumberOfPages(); i < len; i++) {
				BufferedImage image = renderer.renderImageWithDPI(i,
						DEFAULT_DPI);
				int imageHeight = image.getHeight();
				int imageWidth = image.getWidth();
				if (i == 0) {// 计算高度和偏移量
					width = imageWidth;// 使用第一张图片宽度;
					// 保存每页图片的像素值
					imageResult = new BufferedImage(width, imageHeight * len,
							BufferedImage.TYPE_INT_RGB);
				} else {
					shiftHeight += imageHeight; // 计算偏移高度
				}
				singleImgRGB = image.getRGB(0, 0, width, imageHeight, null, 0,
						width);
				imageResult.setRGB(0, shiftHeight, width, imageHeight,
						singleImgRGB, 0, width); // 写入流中
			}
			pdDocument.close();
			String imageName=fileName.replace(".pdf", "_" + DEFAULT_DPI + ".jpg");
			File outFile = new File(FileUtil.FILE_UPLOAD_IMAGE_PATH+imageName);
			ImageIO.write(imageResult, "jpg", outFile);// 写图片
			String htmlName =FileUtil.createFileName(fileName);
			FileOutputStream fOutputStream = new FileOutputStream(filePath +htmlName);
			OutputStreamWriter writer = new OutputStreamWriter(fOutputStream, FileUtil.FILE_ENCODE);
			writer.write(getPdfTemplete(FileUtil.VIEW_IMAGE_DOMAIN + imageName,orgName));
			writer.close();
			return htmlName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}
	/**
	 * 
	 * @time   2018年10月26日 下午2:19:42
	 * @author zuoqb
	 * @param  @param sheetsContent
	 * @param  @param orgName-Excel名称
	 * @param  @return
	 * @return_type   String
	 */
	public static String getPdfTemplete(String imageUrl,String orgName){
		String htmlData  = "";
		try {
			ClassPathResource resource = new ClassPathResource("/docTemplet/pdfTemplates.html");
			InputStream fileInputStream =null;
	        try {
	        	fileInputStream = resource.getInputStream();//这边只能使用getInputStream，其他方法在项目打成jar的时候读取不到文件内容
	            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, FileUtil.FILE_ENCODE));
	            String line;
	            while ((line = br.readLine()) != null) {
	            	htmlData+=line;
	            }
	            htmlData=htmlData.replaceAll("pdf_title_name", orgName);
	            htmlData=htmlData.replaceAll("pdf_name_html_plocehold", imageUrl);
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                fileInputStream.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
		} catch (Exception e) {
		}
		
		return htmlData;
	}
}
