package com.hailian.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hailian.entity.DocUploadFile;
import com.hailian.mapper.DocUploadFileMapper;
import com.hailian.service.IDocUploadFileService;
/**
 * 文件上传记录表服务实现类
 * @author zuoqb123
 * @date 2018-10-24
 */
@Service
@Transactional
public class DocUploadFileServiceImpl extends ServiceImpl<DocUploadFileMapper, DocUploadFile> implements IDocUploadFileService {

    @Autowired
    private DocUploadFileMapper docUploadFileMapper;
   
}
