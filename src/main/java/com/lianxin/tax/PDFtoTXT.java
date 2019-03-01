package com.lianxin.tax;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.derby.tools.sysinfo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.w3c.dom.Document;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.XfaForm;
import com.itextpdf.xmp.XMPIterator;

public class PDFtoTXT {
	//初始化excel
    public static WritableWorkbook wwb = null;
    public static WritableSheet ws;
    public static void main(String[] args) {
        //将当前目录下的pdf文件转成txt文件
    	String path="C:/Users/Administrator/Desktop/lianxin/报税模板/pdf有问题的/大连/";
    	String name="YBNSRZZS 5月";
    	manipulatePdf(path+name+".pdf", path+"哈哈.pdf");
       /* File f = new File(path+name+".pdf"); //获得当前路径
        PDFtoTXT(path,f);
        File txt = new File(path+name+".txt"); //获得当前路径
        TXTtoEXCEL(path,txt);*/
       /* try {
            //从内存中写入文件中
            wwb.write();
            //关闭资源，释放内存
            wwb.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }*/

    }
    public static void manipulatePdf(String src, String dest) {
    	   try {
    		   PdfReader reader = new PdfReader(src);
       	    PdfStamper stamper = new PdfStamper(reader,
       	            new FileOutputStream(dest));
       	    System.out.println(stamper.getUnderContent(1));
       	 XMPIterator i=stamper.getWriter().getXmpWriter().getXmpMeta().iterator();
       	 while (i.hasNext()) {
       		Object type = i.next();
       		System.out.println(type);
			
		}
       	    AcroFields form = stamper.getAcroFields();
       	 System.out.println(stamper.getOverContent(1));
       	    XfaForm xfa = form.getXfa();
       	    Document document= xfa.getDomDocument();
       	    System.out.println(document.getTextContent());
       	    stamper.close();
       	    reader.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
	//将pdf文件输出为txt
    public static void PDFtoTXT(String path,File pdf) {
        PDDocument document;
        BufferedWriter wr;
        try {
            File input = pdf; 
            File output = new File(pdf.getName().split("\\.")[0] + ".txt");
            document = PDDocument.load(input);
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            pdfTextStripper.setStartPage(1);
			pdfTextStripper.setEndPage(1);//只读取第一页
			/*String text = null;
			try {
				text = pdfTextStripper.getText(document);
				System.out.println(text);
			} catch (IOException e) {
				e.printStackTrace();
			}*/
            
            
            wr = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path+output)));
            pdfTextStripper.writeText(document, wr);
            if (document != null) {
            	document.close();
            }
            wr.close();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //爬取目录下的txt文件中关键字并输出到excel文件中
    public static String project = null;
    public static int count = 0;
    public static void TXTtoEXCEL(String path,File txt) {
    	project = "";
        count=0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(txt));
            System.out.println(txt.getName());
            String line = "";

            while((line = br.readLine()) != null) {
                Pattern p0 = Pattern.compile(".*(（一）按适用税率征税销售额).*");
                Matcher m0 = p0.matcher(line);
                if (m0.find()) {
                    System.out.println(line);                   
                    System.out.println("项目值=" + m0.group(1));
                    project = m0.group(1);
                    continue;
                }

                

            }


            if(br != null) {
                br.close();
                br = null;
            }
        } catch (IOException e) {
           e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
