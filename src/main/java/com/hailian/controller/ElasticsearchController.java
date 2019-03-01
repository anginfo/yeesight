package com.hailian.controller;

import io.searchbox.client.JestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hailian.annotation.AuthPower;
import com.hailian.base.BaseController;
import com.hailian.common.PublicResult;
import com.hailian.entity.EsTestEntity;
import com.hailian.enums.PublicResultConstant;
import com.hailian.es.ElasticSearchDao;
/**
 *
 * @date 2018-11-18
 * @author zuoqb123
 * @todo 
 */
@Controller
@Api(value = "Elasticsearch",description="Elasticsearch @author zuoqb123")
public class ElasticsearchController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(ElasticsearchController.class);
    
    @Autowired
	public ElasticSearchDao elasticSearchDao;

	@ResponseBody
	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "保存ES", notes = "保存ES", httpMethod = "GET")
	@RequestMapping(value = "/api/v1/dtax/insert" , method = RequestMethod.GET,produces = { "application/json;charset=UTF-8" })
	public Object save(EsTestEntity e) {
		JestResult r=elasticSearchDao.insert(e, e.INDEX_NAME, e.TYPE);
		return new PublicResult<>(PublicResultConstant.SUCCESS,r.isSucceeded());
	}
	
	@ResponseBody
	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "更新ES", notes = "更新ES", httpMethod = "GET")
	@RequestMapping(value = "/api/v1/dtax/update" , method = RequestMethod.GET,produces = { "application/json;charset=UTF-8" })
	public Object update(EsTestEntity e) {
		JestResult r=elasticSearchDao.update(e, e.INDEX_NAME, e.TYPE);
		return new PublicResult<>(PublicResultConstant.SUCCESS,r.isSucceeded());
	}
	
	@ResponseBody
	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "更新ES", notes = "更新ES", httpMethod = "GET")
	@RequestMapping(value = "/api/v1/dtax/get" , method = RequestMethod.GET,produces = { "application/json;charset=UTF-8" })
	public Object get(String id) {
		JestResult r=elasticSearchDao.getDocument(new EsTestEntity(), EsTestEntity.INDEX_NAME, EsTestEntity.TYPE, id);
		return new PublicResult<>(PublicResultConstant.SUCCESS,r.getJsonMap());
	}
	
	
	@ResponseBody
	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "delete ES", notes = "保delete ES", httpMethod = "GET")
	@RequestMapping(value = "/api/v1/dtax/delete" , method = RequestMethod.GET,produces = { "application/json;charset=UTF-8" })
	public Object delete(String id,String indexName, String type) {
		Object r=elasticSearchDao.delete(id, indexName, type);
		return new PublicResult<>(PublicResultConstant.SUCCESS,r);
	}
	@ResponseBody
	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "保存ES", notes = "保存ES", httpMethod = "GET")
	@RequestMapping(value = "/api/v1/dtax/deleteType" , method = RequestMethod.GET,produces = { "application/json;charset=UTF-8" })
	public Object deleteType() {
		Object r=elasticSearchDao.deleteIndex("index_entity");
		return new PublicResult<>(PublicResultConstant.SUCCESS,r);
	}
	
	
	@ResponseBody
	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "查询ES", notes = "查询ES", httpMethod = "GET")
	@RequestMapping(value = "/api/v1/dtax/searchAllByIndex" , method = RequestMethod.GET,produces = { "application/json;charset=UTF-8" })
	public Object getEs(@RequestParam String indexName) {
		Object o=elasticSearchDao.searchAllByIndex(indexName, new EsTestEntity());
		return new PublicResult<>(PublicResultConstant.SUCCESS,o);
	}
    
	
	@ResponseBody
	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "查询ES", notes = "查询ES", httpMethod = "GET")
	@RequestMapping(value = "/api/v1/dtax/createSearch" , method = RequestMethod.GET,produces = { "application/json;charset=UTF-8" })
	public Object createSearch(@RequestParam String type,@RequestParam String keyWord,@RequestParam String filed) {
		String[] f=new String[]{};
		if(StringUtils.isNotBlank(filed)){
			f=filed.split(",");
		}
		Object o=elasticSearchDao.createSearch(keyWord,type, new EsTestEntity(),f);
		return new PublicResult<>(PublicResultConstant.SUCCESS,o);
	}
   
  
}

