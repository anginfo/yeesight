package com.enterise.web.htmlgen.doc;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;

import com.hailian.utils.FileUtil;

/**
 * 
 * @time   2017年12月28日 下午2:37:41
 * @author zuoqb
 * @todo   将word转html
 */
public class WordToHtml {
	/*
	 * word转html
	 */
	/*public static boolean wordToHtml(final String wordPath) {
	    if (wordPath == null) {// word文档路径为空
	        System.err.println("error:path of word document is null");
	        return false;
	    }
	    final File wordFile = new File(wordPath).getAbsoluteFile(), htmlFile = new File(wordFile.getParent() + "\\PoiPreview.html");
	    try {
	        final InputStream inputStream = new FileInputStream(wordFile);// 输入流
	        final XWPFDocument document = new XWPFDocument(inputStream);// 读取word文档
	        inputStream.close();// 关闭输入流
	        final XHTMLOptions options = XHTMLOptions.create();// 创建选项
	        options.setImageManager(new ImageManager(wordFile.getParentFile(), "PoiImages"));// 设置图片文件夹保存的路径以及文件夹名称
	        final OutputStream outputStream = new FileOutputStream(htmlFile);// 输出流
	        XHTMLConverter.getInstance().convert(document, outputStream, options);// word文档转html
	        System.out.println("html:" + htmlFile.getAbsolutePath());
	        outputStream.close();// 关闭输出流
	        //document.close();// 关闭文档
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	    return true;
	}*/
	/**
	       * 2007版本word转换成html
	       * @throws IOException
	      */
	public static String Word2007ToHtml(String filePath,String fileName,String orgName) throws IOException {
		File f = new File(filePath + fileName);
		if (!f.exists()) {
			System.out.println("Sorry File does not Exists!");
		} else {
			if (f.getName().endsWith(".docx") || f.getName().endsWith(".DOCX")) {

				// 1) 加载word文档生成 XWPFDocument对象  
				InputStream in = new FileInputStream(f);
				XWPFDocument document = new XWPFDocument(in);

				// 2) 解析 XHTML配置 (这里设置IURIResolver来设置图片存放的目录)  
				File imageFolderFile = new File(FileUtil.FILE_UPLOAD_IMAGE_PATH);
				XHTMLOptions options = XHTMLOptions.create().URIResolver(new BasicURIResolver(FileUtil.VIEW_IMAGE_DOMAIN));
				options.setExtractor(new FileImageExtractor(imageFolderFile));
				options.setIgnoreStylesIfUnused(false);
				options.setFragment(true);

				// 3) 将 XWPFDocument转换成XHTML  
				String htmlName=FileUtil.createFileName(fileName);
				OutputStream out = new FileOutputStream(filePath+htmlName);
				XHTMLConverter.getInstance().convert(document, out, options);
				out.close();
				//用自己模板替换上面生成的模板 START
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            XHTMLConverter.getInstance().convert(document, baos, options);
	            String content = baos.toString();
	            content=Jsoup.parse(content).html();
	            FileOutputStream fOutputStream = new FileOutputStream(filePath +htmlName);
	    		OutputStreamWriter writer = new OutputStreamWriter(fOutputStream, FileUtil.FILE_ENCODE);
	    		writer.write(getWordTemplete(content,orgName));
	            baos.close();
	            writer.close();
	          //用自己模板替换上面生成的模板 END
				return htmlName;
			} else {
				System.out.println("只支持Word 2007 格式");
			}
		}
		return null;
	}

	/**
	 * /**
	 * 2003版本word转换成html
	 * @throws IOException
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 */
	public static String Word2003ToHtml(String filePath,String fileName,String orgName) throws IOException, TransformerException,
			ParserConfigurationException {
		InputStream input = new FileInputStream(new File(filePath + fileName));
		HWPFDocument wordDocument = new HWPFDocument(input);
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().newDocument());
		//设置图片存放的位置
		wordToHtmlConverter.setPicturesManager(new PicturesManager() {
			public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches,
					float heightInches) {
				File imgPath = new File(FileUtil.FILE_UPLOAD_IMAGE_PATH);
				if (!imgPath.exists()) {//图片目录不存在则创建
					imgPath.mkdirs();
				}
				File file = new File(FileUtil.FILE_UPLOAD_IMAGE_PATH + suggestedName);
				try {
					OutputStream os = new FileOutputStream(file);
					os.write(content);
					os.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return FileUtil.VIEW_IMAGE_DOMAIN + suggestedName;
			}
		});

		//解析word文档
		wordToHtmlConverter.processDocument(wordDocument);
		Document htmlDocument = wordToHtmlConverter.getDocument();

		String htmlName=FileUtil.createFileName(fileName);
		//也可以使用字符数组流获取解析的内容
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream outStream = new BufferedOutputStream(baos);
		
		//转html
		DOMSource domSource = new DOMSource(htmlDocument);
		StreamResult streamResult = new StreamResult(outStream);

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer serializer = factory.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, FileUtil.FILE_ENCODE);
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "html");

		serializer.transform(domSource, streamResult);
		outStream.close();
		
		//用自己模板替换上面生成的模板 START
		//也可以使用字符数组流获取解析的内容
		String content = baos.toString();
		content=Jsoup.parse(content).html();
		System.out.println(content);
		baos.close();
		FileOutputStream fOutputStream = new FileOutputStream(filePath +htmlName);
		OutputStreamWriter writer = new OutputStreamWriter(fOutputStream, FileUtil.FILE_ENCODE);
		writer.write(getWordTemplete(content,orgName));
		writer.close();
		//用自己模板替换上面生成的模板 END		

		return htmlName;
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
	public static String getWordTemplete(String imageUrl,String orgName){
		String htmlData  = "";
		try {
			ClassPathResource resource = new ClassPathResource("wordTemplates.html");
			InputStream fileInputStream =null;
	        try {
	        	fileInputStream = resource.getInputStream();//这边只能使用getInputStream，其他方法在项目打成jar的时候读取不到文件内容
	            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, FileUtil.FILE_ENCODE));
	            String line;
	            while ((line = br.readLine()) != null) {
	            	htmlData+=line;
	            }
	            htmlData=htmlData.replaceAll("word_title_name", orgName);
	            htmlData=htmlData.replaceAll("word_name_html_plocehold", imageUrl);
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
