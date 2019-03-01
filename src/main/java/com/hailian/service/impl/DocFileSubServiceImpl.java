package com.hailian.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hailian.entity.DocFileSub;
import com.hailian.mapper.DocFileSubMapper;
import com.hailian.service.IDocFileSubService;
/**
 * 文件编码表服务实现类
 * @author zuoqb123
 * @date 2018-11-12
 */
@Service
@Transactional
public class DocFileSubServiceImpl extends ServiceImpl<DocFileSubMapper, DocFileSub> implements IDocFileSubService {

    @Autowired
    private DocFileSubMapper docFileSubMapper;
   
}
