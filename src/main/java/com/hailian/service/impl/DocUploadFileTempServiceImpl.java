package com.hailian.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hailian.entity.DocUploadFile;
import com.hailian.entity.DocUploadFileTemp;
import com.hailian.mapper.DocUploadFileMapper;
import com.hailian.mapper.DocUploadFileTempMapper;
import com.hailian.service.IDocUploadFileTempService;
/**
 * 文件上传目录表服务实现类
 * @author zuoqb123
 * @date 2018-11-10
 */
@Service
@Transactional
public class DocUploadFileTempServiceImpl extends ServiceImpl<DocUploadFileTempMapper, DocUploadFileTemp> implements IDocUploadFileTempService {
	List<String> cwUser=Arrays.asList("01516001","01017619","01188285","01515959","01516001");
    @Autowired
    private DocUploadFileTempMapper docUploadFileTempMapper;
    @Autowired
    private DocUploadFileMapper docUploadFileMapper;
	@Override
	public List<DocUploadFileTemp> findList(DocUploadFileTemp temp) {
		List<DocUploadFileTemp> list=docUploadFileTempMapper.findList(temp);
		//查询目录下文件
		for(DocUploadFileTemp t:list){
			EntityWrapper<DocUploadFile> fileWrapper = new EntityWrapper<DocUploadFile>();
			fileWrapper.where("del_flag={0}", 0);
			fileWrapper.eq("temp_id", t.getId());
			fileWrapper.orderBy("create_date", false);
			t.setFiles(docUploadFileMapper.selectList(fileWrapper));
			//查询子目录
			DocUploadFileTemp c=new DocUploadFileTemp();
			c.setCreateBy(temp.getCreateBy());
			c.setTempName(temp.getTempName());
			c.setIsPublic(temp.getIsPublic());
			c.setIsAdmin(temp.getIsAdmin());
			c.setParentId(t.getId());
			List<DocUploadFileTemp> children=findList(c);
			t.setChildren(children);
		}
		return list;
	}
	@Override
	public List<DocUploadFileTemp> findAllList(DocUploadFileTemp temp) {
		List<DocUploadFileTemp> finalTemp=new ArrayList<DocUploadFileTemp>();
		//查询全部
		temp.setParentId("");
		List<DocUploadFileTemp> fileTemps=docUploadFileTempMapper.findList(temp);
		EntityWrapper<DocUploadFile> fileWrapper = new EntityWrapper<DocUploadFile>();
		fileWrapper.where("del_flag={0}", 0);
		fileWrapper.orderBy("create_date", false);
		List<DocUploadFile> files=docUploadFileMapper.selectList(fileWrapper);
		//重新排序
		finalTemp=getTempByPid("0", fileTemps, files,temp.getCreateBy());
		return finalTemp;
	}
	public List<DocUploadFileTemp> getTempByPid(String pid,List<DocUploadFileTemp> fileTemps,List<DocUploadFile> files,String userCode){
		List<DocUploadFileTemp> temps=new ArrayList<DocUploadFileTemp>();
		List<DocUploadFileTemp> others=new ArrayList<DocUploadFileTemp>();
		for(DocUploadFileTemp temp:fileTemps){
			if(temp.getParentId().equals(pid)){
				//设置文件
				List<DocUploadFile> fs=new ArrayList<DocUploadFile>();
				for(DocUploadFile f:files){
					if(StringUtils.isNotBlank(f.getTempId())&&f.getTempId().equals(temp.getId())){
						fs.add(f);
					}
				}
				temp.setFiles(fs);
				temps.add(temp);
			}else{
				others.add(temp);
			}
		}
		if("S00870_WD1100001".equals(pid)){
			//如果是三级
			if(cwUser.contains(userCode)){
				//重新排序 将财务放第一
				for(DocUploadFileTemp temp:temps){
					if("财务".equals(temp.getTempName())){
						temps.remove(temp);
						temps.add(0, temp);
						break;
					}
				}
			}
		}
		for(DocUploadFileTemp temp:temps){
			//查询子
			if(others.size()>0){
				List<DocUploadFileTemp> c=getTempByPid(temp.getId(), others, files,userCode);
				temp.setChildren(c);
			}
		}
		return temps;
	}
   
}
