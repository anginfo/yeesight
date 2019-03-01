package com.hailian.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.hailian.entity.DocUploadFileTemp;


/**
 * 文件上传目录表服务类
 * @author zuoqb123
 * @date 2018-11-10
 */
public interface IDocUploadFileTempService extends IService<DocUploadFileTemp> {
	List<DocUploadFileTemp> findList(DocUploadFileTemp temp);
	List<DocUploadFileTemp> findAllList(DocUploadFileTemp temp);
}
