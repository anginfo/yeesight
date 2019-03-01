package com.hailian.mapper;
import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hailian.entity.DocUploadFileTemp;

/**
  * 文件上传目录表Mapper接口
 * @author zuoqb123
 * @date 2018-11-10
 */
public interface DocUploadFileTempMapper extends BaseMapper<DocUploadFileTemp> {
	List<DocUploadFileTemp> findList(DocUploadFileTemp temp);
	
}


