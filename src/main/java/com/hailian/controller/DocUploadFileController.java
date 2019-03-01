package com.hailian.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import com.enterise.web.htmlgen.doc.Word2PdfUtil;
import com.enterise.web.htmlgen.pdf.PdfToImage;
import com.enterise.web.htmlgen.ppt.Ppt2PdfUtils;
import com.enterise.web.htmlgen.xls.ExcelToHtml;
import com.github.pagehelper.PageInfo;
import com.hailian.annotation.AuthPower;
import com.hailian.annotation.Log;
import com.hailian.base.BaseController;
import com.hailian.common.PublicResult;
import com.hailian.common.UUIDUtils;
import com.hailian.conf.Constant;
import com.hailian.entity.DocFileCode;
import com.hailian.entity.DocFileSub;
import com.hailian.entity.DocUploadFile;
import com.hailian.entity.DocUploadFileTemp;
import com.hailian.enums.PublicResultConstant;
import com.hailian.service.IDocFileCodeService;
import com.hailian.service.IDocFileSubService;
import com.hailian.service.IDocUploadFileService;
import com.hailian.service.IDocUploadFileTempService;
import com.hailian.utils.FileUtil;
import com.hailian.utils.Pinyin;
/**
 *
 * @author zuoqb123
 * @date 2018-10-24
 * @todo 文件上传记录表路由
 */
@RestController
@RequestMapping("/api/v1/docUploadFile")
@Api(value = "文件上传记录表",description="文件上传记录表 @author zuoqb123")
public class DocUploadFileController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(DocUploadFileController.class);

    @Autowired
    public IDocUploadFileService iDocUploadFileService;
    @Autowired
    public IDocUploadFileTempService iDocUploadFileTempService;
    @Autowired
    public IDocFileCodeService iDocFileCodeService;
    @Autowired
    public IDocFileSubService iDocFileSubService;
    
    
    /**
     * @date   2018-10-16
     * @author zuoqb123
     * @todo   文件上传
     */
  	@ApiOperation(value = "文件上传", notes = "文件上传", httpMethod = "PUT")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@RequestMapping(value = "/uploadDoc", method = RequestMethod.PUT,produces = { "application/json;charset=UTF-8" })
    public Object uploadDoc(HttpServletRequest request,
			@RequestParam(value = "file", required = true) MultipartFile file,@RequestParam(value="tempId",required = false) String tempId,
			@RequestParam(value="userCode",required = false) String userCode) {
		try {
			DocUploadFile entity=new DocUploadFile();
			// 获取文件名
			String fileName = file.getOriginalFilename();
			String fileType=FileUtil.getFileExt(fileName); 
			int index=fileName.lastIndexOf('.');
			fileName=fileName.substring(0,index);
			// 进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
			long size = file.getSize();
			if (StringUtils.isEmpty(fileName) || size == 0) {
				return new PublicResult<>(PublicResultConstant.ERROR,"文件不能为空！", null);
			}
			if("doc".equals(fileType)||"docx".equals(fileType)
					||"xls".equals(fileType)||"xlsx".equals(fileType)
					||"pdf".equals(fileType)||"png".equals(fileType)||"jpg".equals(fileType)
					||"ppt".equals(fileType)||"pptx".equals(fileType)){
				InputStream is = null;
				//根据新建的文件实例化输入流
				is = file.getInputStream();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
				String dateStr=sdf.format(new Date());
				String uploadName=fileName+"("+dateStr+")."+fileType;
				String filePath=FileUtil.savePic(is,FileUtil.FILE_UPLOAD_IMAGE_PATH+ File.separator+Constant.DEFAULT_UPLOAD_TEMP_NAME, uploadName);
				String htmlName="";//转化后html页面名称
				//System.out.println(fileType);
				try {
					switch (fileType) {
					case "xls":
						htmlName=ExcelToHtml.excel2Html(filePath+ File.separator,uploadName,fileName);
						break;
					case "xlsx":
						htmlName=ExcelToHtml.excel2Html(filePath+ File.separator,uploadName,fileName);
						break;
						/*case "doc":
						htmlName=WordToHtml.Word2003ToHtml(filePath+ File.separator, uploadName,fileName);
						break;
					case "docx":
						htmlName=WordToHtml.Word2007ToHtml(filePath+ File.separator, uploadName,fileName);
						break;*/
					case "doc":
						String outNameDoc=fileName+"("+dateStr+").pdf";
						boolean successDoc=Word2PdfUtil.doc2pdf(filePath+ File.separator+uploadName, filePath+ File.separator+outNameDoc);
						if(successDoc){
							htmlName=PdfToImage.pdfToImage(filePath+ File.separator, outNameDoc,fileName);
						}
						break;
					case "docx":
						String outName=fileName+"("+dateStr+").pdf";
						boolean success=Word2PdfUtil.doc2pdf(filePath+ File.separator+uploadName, filePath+ File.separator+outName);
						if(success){
							htmlName=PdfToImage.pdfToImage(filePath+ File.separator, outName,fileName);
						}
						break;
					case "pdf":
						/*PdfToHtml pdf2Html = new PdfToHtml(filePath+ File.separator,uploadName);
						htmlName=pdf2Html.generate();*/
						htmlName=PdfToImage.pdfToImage(filePath+ File.separator, uploadName,fileName);
						break;
					case "ppt":
						String outNameDocPPt=fileName+"("+dateStr+").pdf";
						boolean successPpt=Ppt2PdfUtils.ppt2Pdf(filePath+ File.separator+uploadName, filePath+ File.separator+outNameDocPPt,fileType);
						if(successPpt){
							htmlName=PdfToImage.pdfToImage(filePath+ File.separator, outNameDocPPt,fileName);
						}
						break;
					case "pptx":
						String outNamePptx=fileName+"("+dateStr+").pdf";
						boolean successPptx=Ppt2PdfUtils.ppt2Pdf(filePath+ File.separator+uploadName, filePath+ File.separator+outNamePptx,fileType);
						if(successPptx){
							htmlName=PdfToImage.pdfToImage(filePath+ File.separator, outNamePptx,fileName);
						}
						break;
					default:
						break;
					}
				} catch (Exception e) {
				}
				//生成编码
				if(StringUtils.isNotBlank(tempId)){
					entity.setCode(createFileCode(tempId, fileName, "1"));
				};
				
				entity.setId(UUIDUtils.getUuid());
				entity.setCreateDate(new Date());
				if(StringUtils.isBlank(userCode)){
					entity.setCreateBy(getLoginUser(request).getId());
				}else{
					entity.setCreateBy(userCode);
				}
				entity.setOriginalname(fileName);
				entity.setUploadName(uploadName);
				entity.setExt(fileType);
				entity.setPath(filePath);
				entity.setSize(Integer.valueOf(size+""));
				entity.setHtmlName(htmlName);
				entity.setTempId(tempId);
				if(iDocUploadFileService.insert(entity)){
					return new PublicResult<>(PublicResultConstant.SUCCESS, entity);
				}else{
					return new PublicResult<>(PublicResultConstant.ERROR, null);
				}
			}else{
				return new PublicResult<>(PublicResultConstant.ERROR,"请选择Excel、Word、Pdf文件", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(),null);
		}
	}
  	
  	/**
	 * 
	 * @time   2018年10月9日 下午12:59:51
	 * @author zuoqb
	 * @todo  下载文档
	 */
	@ApiOperation(value = "文件下载", notes = "文件下载", httpMethod = "GET")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
	@RequestMapping(value = "/dowloadDoc", method = RequestMethod.GET,produces = { "application/json;charset=UTF-8" })
	public void dowloadDoc(HttpServletResponse response,@RequestParam(value="fileId",required = true) String fileId) {
		DocUploadFile uploadFile=iDocUploadFileService.selectById(fileId);
		if(uploadFile==null){
			return;
		}
		String path=null;
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			path = URLDecoder.decode(uploadFile.getPath()+File.separator+uploadFile.getUploadName(),FileUtil.FILE_ENCODE);
			File file = new File(path);
			String fileName=file.getName();    
			String fileTyle=FileUtil.getFileExt(fileName).toLowerCase(); 
			if("xls".equals(fileTyle)||"xlsx".equals(fileTyle)){
				response.setContentType("application/msexcel");
			}
			if("doc".equals(fileTyle)||"docx".equals(fileTyle)){
				response.setContentType("application/msword");
			}
			if("pdf".equals(fileTyle)){
				response.setContentType("application/pdf");
			}
			if("ppt".equals(fileTyle)){
				response.setContentType("application/ms-powerpoint");
			}
			if("txt".equals(fileTyle)){
				response.setContentType("text/txt");
			}
			if("zip".equals(fileTyle)){
				response.setContentType("application/zip");
			}
			if("htm".equals(fileTyle)||"html".equals(fileTyle)){
				response.setContentType("text/html");
			}
			if("rm".equals(fileTyle)||"ram".equals(fileTyle)){
				response.setContentType("audio/x-pn-realaudio");
			}
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String((uploadFile.getUploadName()).getBytes(), "iso-8859-1"));
			os = response.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(file));
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}    
    /**
     * @date   2018-10-24
     * @author zuoqb123
     * @todo   新增文件上传记录表
     */
  /*	@ApiOperation(value = "新增文件上传记录表", notes = "新增文件上传记录表", httpMethod = "POST")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request,DocUploadFile entity,
			@RequestParam(value="userCode",required = true) String userCode) {
		try {
			if(StringUtils.isBlank(entity.getId())){
				//新增
				entity.setId(UUIDUtils.getUuid());
				entity.setCreateDate(new Date());
				if(StringUtils.isNotBlank(userCode)){
					entity.setCreateBy(userCode);
				}else{
					entity.setCreateBy(getLoginUser(request).getId());
				}
				if(iDocUploadFileService.insert(entity)){
					return new PublicResult<>(PublicResultConstant.SUCCESS, entity);
				}else{
					return new PublicResult<>(PublicResultConstant.ERROR, null);
				}
			}else{
				return new PublicResult<>(PublicResultConstant.PARAM_ERROR, "新增主键必须为空!",null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(), null);
		}
	}*/
    
    /**
     * @date   2018-10-24
     * @author zuoqb123
     * @todo   删除文件上传记录表
     */
  	@ApiOperation(value = "删除文件上传记录表", notes = "删除文件上传记录表", httpMethod = "POST")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST,produces = { "application/json;charset=UTF-8" })
	public Object delete(HttpServletRequest request,@PathVariable("id") String id,
			@RequestParam(value="userCode",required = true) String userCode) {
		try {
			DocUploadFile entity=new DocUploadFile();
			entity.setId(id);
			entity.setDelFlag(DEL_FLAG);
			DocUploadFile file=iDocUploadFileService.selectById(id);
			if(file==null){
				return new PublicResult<>(PublicResultConstant.ERROR, "文件不存在!");
			}
			if(file.getCreateBy()!=null&&!file.getCreateBy().equals(userCode)){
				return new PublicResult<>(PublicResultConstant.ERROR, "该文件只能创建人进行操作!");
			}
			entity.setUpdateDate(new Date());
			entity.setUpdateBy(userCode);
			 if(iDocUploadFileService.updateById(entity)){
				 return new PublicResult<>(PublicResultConstant.SUCCESS, null);
			 }else{
				 return new PublicResult<>(PublicResultConstant.ERROR, null);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(), null);
		}
	}
	
	 /**
     * @date   2018-10-24
     * @author zuoqb123
     * @todo   更新文件上传记录表
     */
  	@ApiOperation(value = "更新文件上传记录表", notes = "更新文件上传记录表", httpMethod = "PUT")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request,DocUploadFile entity,
			@RequestParam(value="userCode",required = true) String userCode) {
		try {
			if(entity!=null&&StringUtils.isNotBlank(entity.getId())){
				//更新
				DocUploadFile file=iDocUploadFileService.selectById(entity.getId());
				if(file==null){
					return new PublicResult<>(PublicResultConstant.ERROR, "文件不存在!");
				}
				if(!file.getCreateBy().equals(userCode)){
					return new PublicResult<>(PublicResultConstant.ERROR, "该文件只能创建人进行操作!");
				}
				entity.setUpdateDate(new Date());
				entity.setUpdateBy(userCode);
				if(iDocUploadFileService.updateById(entity)){
					return new PublicResult<>(PublicResultConstant.SUCCESS, entity);
				}else{
					return new PublicResult<>(PublicResultConstant.ERROR, null);
				}
			}else{
				return new PublicResult<>(PublicResultConstant.PARAM_ERROR, "修改主键不能为空!",null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(), null);
		}
	}
    
    
    /**
     * @date   2018-10-24
     * @author zuoqb123
     * @todo   查询单个文件上传记录表
     */
  	@ApiOperation(value = "查询单个文件上传记录表", notes = "查询单个文件上传记录表", httpMethod = "GET")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET ,produces = { "application/json;charset=UTF-8" })
  	public PublicResult<DocUploadFile> get(HttpServletRequest request,@PathVariable("id") String id) {
  		DocUploadFile entity=null;
  		try {
  			EntityWrapper<DocUploadFile> wrapper = new EntityWrapper<DocUploadFile>();
  			wrapper.where("del_flag={0}", UN_DEL_FLAG);
  			wrapper.eq("id", id);
  			entity=iDocUploadFileService.selectOne(wrapper);
  			return new PublicResult<>(PublicResultConstant.SUCCESS, entity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(), null);
		}
  	}
	
  	 /**
     * @date   2018-10-24
     * @author zuoqb123
     * @todo   分页查询文件上传记录表
     */
  	@ApiOperation(value = "分页查询文件上传记录表", notes = "分页查询文件上传记录表", httpMethod = "GET")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@RequestMapping(value = "/list", method = RequestMethod.GET,produces = { "application/json;charset=UTF-8" })
    public PublicResult<PageInfo<DocUploadFile>> list(DocUploadFile entity,@RequestParam(value="pageNum",required = false,defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize",required = false,defaultValue="10") Integer pageSize,HttpServletRequest request) {
		try {
			EntityWrapper<DocUploadFile> wrapper = searchWrapper(request, entity);
			PageHelper.startPage(pageNum, pageSize);
			List<DocUploadFile> list = iDocUploadFileService.selectList(wrapper);
			PageInfo<DocUploadFile> page = new PageInfo<DocUploadFile>(list);
			return new PublicResult<>(PublicResultConstant.SUCCESS, page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(),null);
		}

	}
 
  
  	
  	/**
	 * 
	 * @time   2018年10月9日 下午12:59:51
	 * @author zuoqb
	 * @todo   文件预览
	 */
  	@ApiOperation(value = "文件预览", notes = "文件预览", httpMethod = "GET")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
	@GetMapping(value = "/viewFile", produces = { "application/json;charset=UTF-8" })
	@Log(description = "文件预览接口:/api/docUploadFile/viewFile")
	public void viewFile(HttpServletResponse response,HttpServletRequest request,@RequestParam(value="id",required = true) String id,
			@RequestParam(value="waterMark",required = false,defaultValue="青岛海联软件") String waterMark) {
		BufferedInputStream bis = null;
		OutputStream os = null;
		byte[] buff = new byte[1024];
		try {
			EntityWrapper<DocUploadFile> wrapper = new EntityWrapper<DocUploadFile>();
  			wrapper.where("del_flag={0}", UN_DEL_FLAG);
  			wrapper.eq("id", id);
  			DocUploadFile entity=iDocUploadFileService.selectOne(wrapper);
  			File file=null;
  			if(entity!=null){
  				switch (entity.getExt()) {
				/*case "pdf":
					//PDF预览
					response.setContentType("application/pdf");
  					file = new File(entity.getPath()+ File.separator +entity.getUploadName());
  					os = response.getOutputStream();
  					if(file.isFile() && file.exists()){ //判断文件是否存在
  						os = response.getOutputStream();
  						bis = new BufferedInputStream(new FileInputStream(file));
  						int i = bis.read(buff);
  						while (i != -1) {
  							os.write(buff, 0, buff.length);
  							os.flush();
  							i = bis.read(buff);
  						}
  					}else{
  						response.sendError(response.SC_NOT_FOUND, "file does not exist");
  					}
					break;*/
				case "png":
					bis = viewImage(response, bis, buff, entity);
					break;
				case "jpg":
					bis = viewImage(response, bis, buff, entity);
					break;
				default:
					//HTML预览(word、excel转化后的)
					response.setContentType("text/html");
	  				file = new File(entity.getPath()+ File.separator +entity.getHtmlName());
	  				os = response.getOutputStream();
	  				if(file.isFile() && file.exists()){ //判断文件是否存在
	                    InputStreamReader read = new InputStreamReader(
	                    new FileInputStream(file),FileUtil.FILE_ENCODE);//考虑到编码格式
	                    BufferedReader bufferedReader = new BufferedReader(read);
	                    String lineTxt = null;
	                    String content="";
	                    while((lineTxt = bufferedReader.readLine()) != null){
	                        content+=lineTxt;
	                    }
	                    content=content.replaceAll("1169_water_mark", waterMark);
	                    os.write(content.getBytes(FileUtil.FILE_ENCODE));
	                    read.close();
					break;
	  				}
  				}
  			}else{
				response.sendError(response.SC_NOT_FOUND, "file does not exist");
  			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
  	
  	/**
	 * 
	 * @time   2018年10月9日 下午12:59:51
	 * @author zuoqb
	 * @todo   图片预览
	 */
  	@ApiOperation(value = "图片预览", notes = "图片预览", httpMethod = "GET")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
	@GetMapping(value = "/viewImage", produces = { "application/json;charset=UTF-8" })
	//@Log(description = "API接口:/api/docUploadFile/viewImage")
	public void viewImage(HttpServletResponse response,HttpServletRequest request,@RequestParam(value="name",required = true) String fileName) {
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			response.setContentType("image/jpeg");
			File file = new File(FileUtil.FILE_UPLOAD_IMAGE_PATH+fileName);
			if(file.isFile() && file.exists()){ //判断文件是否存在
				os = response.getOutputStream();
				bis = new BufferedInputStream(new FileInputStream(file));
				int i = bis.read(buff);
				while (i != -1) {
					os.write(buff, 0, buff.length);
					os.flush();
					i = bis.read(buff);
				}
			}else{
				response.setContentType("text/html");
				response.sendError(response.SC_NOT_FOUND, "file does not exist");
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
  	
  

	protected BufferedInputStream viewImage(HttpServletResponse response,
			BufferedInputStream bis, byte[] buff, DocUploadFile entity)
			throws IOException, FileNotFoundException {
		OutputStream os;
		File file;
		response.setContentType("image/jpeg");
		file = new File(entity.getPath()+ File.separator +entity.getUploadName());
		if(file.isFile() && file.exists()){ //判断文件是否存在
			os = response.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(file));
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
		}else{
			response.sendError(response.SC_NOT_FOUND, "file does not exist");
		}
		return bis;
	}
	public String createFileCode(String tempId, String fileName, String type) {
		DocUploadFileTemp temp=iDocUploadFileTempService.selectById(tempId);
		if(temp==null){
			return null;
		}
		String code="RRS_DM_";
		String sub2=Pinyin.getPinYinHeadChar(temp.getTempName());
		code+=sub2+"_";
		String sub3="";
		List<DocFileSub> subList=iDocFileSubService.selectList(null);
		for(DocFileSub sub:subList){
			if(fileName.indexOf(sub.getName())!=-1){
				sub3=sub.getCode();
				break;
			}
		}
		code+=sub3;
		//查询序号
		int maxNums=iDocFileCodeService.getMaxNums(type, "RRS", sub2, sub3)+1;
		//处理序号
		String all="000000000000000000000000000000"+maxNums;
		
		code+=all.substring(all.length()-6, all.length());
		DocFileCode docFileCode=new DocFileCode();
		docFileCode.setType(type);
		docFileCode.setSub1("RRS");
		docFileCode.setSub2(sub2);
		docFileCode.setSub3(sub3);
		docFileCode.setNums(maxNums);
		docFileCode.setCode(code);
		iDocFileCodeService.insert(docFileCode);
		return code;
	}
  	
	   /**
     * @date   2018年9月25日 下午5:36:10
     * @author zuoqb123
     * @todo   构建查询条件-以后扩展
     */
    private EntityWrapper<DocUploadFile> searchWrapper(HttpServletRequest request, DocUploadFile entity) {
		EntityWrapper<DocUploadFile> wrapper = new EntityWrapper<DocUploadFile>();
		wrapper.where("del_flag={0}", UN_DEL_FLAG);
		if(getLoginUser(request)!=null&&StringUtils.isNotBlank(getLoginUser(request).getId())){
			if(!isAdmin(request))
			 wrapper.and("create_by", getLoginUser(request).getId());
		}
				//根据编码模糊查询
		if(entity.getId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getId()))){
			wrapper.like("id", String.valueOf(entity.getId()));
		}
				//根据原名称模糊查询
		if(entity.getOriginalname()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getOriginalname()))){
			wrapper.like("originalname", String.valueOf(entity.getOriginalname()));
		}
				//根据服务器文件路径模糊查询
		if(entity.getPath()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getPath()))){
			wrapper.like("path", String.valueOf(entity.getPath()));
		}
				//根据后缀模糊查询
		if(entity.getExt()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getExt()))){
			wrapper.like("ext", String.valueOf(entity.getExt()));
		}
				//根据上传后文件名称模糊查询
		if(entity.getUploadName()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getUploadName()))){
			wrapper.like("upload_name", String.valueOf(entity.getUploadName()));
		}
				//根据大小模糊查询
		if(entity.getSize()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getSize()))){
			wrapper.like("size", String.valueOf(entity.getSize()));
		}
				//根据转化的html名称模糊查询
		if(entity.getHtmlName()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getHtmlName()))){
			wrapper.like("html_name", String.valueOf(entity.getHtmlName()));
		}
				//根据预览文件地址模糊查询
		if(entity.getViewUrl()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getViewUrl()))){
			wrapper.like("view_url", String.valueOf(entity.getViewUrl()));
		}
				//根据备注信息模糊查询
		if(entity.getRemarks()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getRemarks()))){
			wrapper.like("remarks", String.valueOf(entity.getRemarks()));
		}
				//根据创建者模糊查询
		if(entity.getCreateBy()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getCreateBy()))){
			wrapper.eq("create_by", String.valueOf(entity.getCreateBy()));
		}
				//根据创建时间模糊查询
		if(entity.getCreateDate()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getCreateDate()))){
			wrapper.like("create_date", String.valueOf(entity.getCreateDate()));
		}
				//根据更新者模糊查询
		if(entity.getUpdateBy()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getUpdateBy()))){
			wrapper.like("update_by", String.valueOf(entity.getUpdateBy()));
		}
				//根据更新时间模糊查询
		if(entity.getUpdateDate()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getUpdateDate()))){
			wrapper.like("update_date", String.valueOf(entity.getUpdateDate()));
		}
				//根据删除标记（0：正常；1：删除）模糊查询
		if(entity.getDelFlag()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getDelFlag()))){
			wrapper.like("del_flag", String.valueOf(entity.getDelFlag()));
		}
				wrapper.orderBy("create_date", false);
		System.out.println(wrapper.originalSql());
		return wrapper;
	}
    
}

