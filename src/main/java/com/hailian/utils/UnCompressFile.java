package com.hailian.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;

/**
 * 
 * @time   2017年12月12日 下午12:38:15
 * @author zuoqb
 * @todo   解压文件
 */
public class UnCompressFile {
	private static UnCompressFile instance = new UnCompressFile();
	private static final int BUFFEREDSIZE = 1024;

	private UnCompressFile() {
	}

	public static UnCompressFile getInstance() {
		return instance;
	}

	/**
	 * 解压zip或者rar包的内容到指定的目录下，可以处理其文件夹下包含子文件夹的情况
	 *
	 * @param zipFilename
	 *            要解压的zip或者rar包文件
	 * @param outputDirectory
	 *            解压后存放的目录
	 */
	@SuppressWarnings("rawtypes")
	public static synchronized void unzip(String zipFilename, String outputDirectory) throws IOException {
		File outFile = new File(outputDirectory);
		if (!outFile.exists()) {
			outFile.mkdirs();
		}
		ZipFile _zipFile = null;
		try {
			_zipFile=new ZipFile(zipFilename,Charset.forName("UTF-8"));
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			_zipFile=new ZipFile(zipFilename,Charset.forName("GBK"));
		}
		for( Enumeration entries = _zipFile.entries() ; entries.hasMoreElements() ; ){
            ZipEntry entry = (ZipEntry)entries.nextElement() ;
            File _file = new File(outputDirectory + File.separator + entry.getName()) ;
            if( entry.isDirectory() ){
                _file.mkdirs() ;
            }else{
                File _parent = _file.getParentFile() ;
                if( !_parent.exists() ){
                    _parent.mkdirs() ;
                }
                InputStream _in = _zipFile.getInputStream(entry);
                OutputStream _out = new FileOutputStream(_file) ;
                int len ;
                byte[] by = new byte[BUFFEREDSIZE];
                while( (len = _in.read(by)) > 0){
                    _out.write(by, 0, len);
                }
                if (null !=_out){
                    _out.flush();
                    _out.close();
                }
                if (null !=_in){
                    _in.close();
                }
            }
        }
		// 加了这行代码之后就可以删除了
        _zipFile.close();// 加了这行代码之后就可以删除了
	}
	/**  
    * 解压rar格式压缩包。  
    * 对应的是java-unrar-0.3.jar，但是java-unrar-0.3.jar又会用到commons-logging-1.1.1.jar  
    */   
   public static void unrar(String sourceRar,String destDir) throws Exception{    
       Archive a = null;    
       FileOutputStream fos = null;    
       try{    
           a = new Archive(new File(sourceRar));    // a = new Archive(new File(sourceRar));
           FileHeader fh = a.nextFileHeader();    
           while(fh!=null){    
               if(!fh.isDirectory()){    
                   //1 根据不同的操作系统拿到相应的 destDirName 和 destFileName    
                   String compressFileName = fh.getFileNameString().trim();   
                   compressFileName=new String(compressFileName.getBytes("utf-8"),"utf-8");
                   String destFileName = "";    
                   String destDirName = "";    
                   //非windows系统    
                   if(File.separator.equals("/")){    
                       destFileName = destDir +File.separator + compressFileName.replaceAll("\\\\", "/").replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5.，,。？“”]+","");    
                       destDirName = destFileName.substring(0, destFileName.lastIndexOf("/"));    
                   //windows系统     
                   }else{    
                       destFileName = destDir+	File.separator + compressFileName.replaceAll("/", "\\\\").replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5.，,。？“”]+", "");    
                       destDirName = destFileName.substring(0, destFileName.lastIndexOf("\\"));    
                   }    
                   //2创建文件夹    
                   File dir = new File(destDirName);    
                   if(!dir.exists()||!dir.isDirectory()){    
                       dir.mkdirs();    
                   }    
                   //3解压缩文件    
                   fos = new FileOutputStream(new File(destFileName));    
                   a.extractFile(fh, fos);    
                   fos.close();    
                   fos = null;    
               }    
               fh = a.nextFileHeader();    
           }    
           a.close();    
           a = null;    
       }catch(Exception e){    
           throw e;    
       }finally{    
           if(fos!=null){    
               try{fos.close();fos=null;}catch(Exception e){e.printStackTrace();}    
           }    
           if(a!=null){    
               try{a.close();a=null;}catch(Exception e){e.printStackTrace();}    
           }    
       }    
   }    
	/**
	 * 
	 * @time   2018年1月16日 下午5:26:48
	 * @author zuoqb
	 * @todo   读取压缩包下文件列表
	 * @param  @param zipFilename
	 * @param  @return
	 * @param  @throws IOException
	 * @return_type   List<String>
	 */
	@SuppressWarnings("rawtypes")
	public static synchronized List<String> zipFiles(String zipFilename) throws IOException {
		List<String> files=new ArrayList<String>();
		ZipFile _zipFile = null;
		try {
			_zipFile=new ZipFile(zipFilename,Charset.forName("UTF-8"));
		} catch (Exception e) {
		}finally{
			_zipFile=new ZipFile(zipFilename,Charset.forName("GBK"));
		}
		for( Enumeration entries = _zipFile.entries() ; entries.hasMoreElements() ; ){
            ZipEntry entry = (ZipEntry)entries.nextElement() ;
            files.add(entry.getName());
           System.out.println( entry.getName());
        }
		_zipFile.close();
		return files;
	}

}
