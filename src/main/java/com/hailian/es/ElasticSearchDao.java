package com.hailian.es;

import io.searchbox.client.JestResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.core.SuggestResult;

import java.util.List;

import org.elasticsearch.index.mapper.ParseContext.Document;
import org.springframework.data.elasticsearch.core.query.Criteria;

import com.google.gson.JsonObject;

public interface ElasticSearchDao {
	 /**
     * 删除索引
     * @param type ：当前删除document名称
     * @return
     */
    public boolean deleteIndex(String type) ;

    //清除缓存
    public JestResult clearCache() ;

    /**
     * 关闭索引
     * @param type ：文档表示的对象类别
     * @return
     */
    public JestResult closeIndex(String type) ;

    //优化索引
    public JestResult optimizeIndex() ;

    //刷新索引
    public JestResult flushIndex();

    //判断索引是否存在
    public JestResult indicesExists(String indexName);

    //查看节点信息
    public JestResult nodesInfo();

    //查看集群健康信息
    public JestResult health();

    //节点状态
    public JestResult nodesStats();

    /**
     * 更新Document
     * @param index ：文档在哪存放
     * @param type ： 文档表示的对象类别
     * @param id ：文档唯一标识
     */
    public JestResult updateDocument(String script , String index,String type,String id);

    /**
     * 删除Document
     * @param index ：文档在哪存放
     * @param type ： 文档表示的对象类别
     * @param id ：文档唯一标识
     * @return
     */
    public JestResult deleteDocument(String index,String type,String id) ;

/**
     * 根据条件删除
     * @param index
     * @param type
     * @param params
     */
    public JestResult deleteDocumentByQuery(String index, String type, String params);


    /**
     * 获取Document
     * @param o ：返回对象
     * @param index ：文档在哪存放
     * @param type ： 文档表示的对象类别
     * @param id ：文档唯一标识
     * @return
     */
    public <T> JestResult getDocument(T o , String index , String type , String id) ;

    //Suggestion
    public List<SuggestResult.Suggestion> suggest() ;

    /**
     * 查询全部
     * @param index ：文档在哪存放
     * @return
     */
    public <T> List<Hit<T,Void>> searchAllByIndex(String index , T o);

    /**
     * 搜索
     * @param keyWord ：搜索关键字
     * @return 
     */
    public <T> List<Hit<T,Void>> createSearch(String keyWord , String type , T o , String... fields) ;

    //bulkIndex操作
    public <T> void bulkIndex(String index , String type , T o) ;

    /**
     * 创建索引
     * @param o ：返回对象
     * @param index ：文档在哪存放
     * @param type ： 文档表示的对象类别
     * @return
     */
    public <T> JestResult createIndex(T o , String index , String type);

    /**
     * 搜索事件流图表数据
     * @param param
     * @return
     */
    public JsonObject searchEvent(String param); 
    
    /**
     * 保存数据
     */
    public <T> JestResult insert(T o,String indexName,String type);
    public <T> JestResult update(T o,String indexName,String type);
    public <T> boolean delete(String id,String indexName, String type);
    public <T> Document searchById(String id,String indexName, String type);
    /**
     * 查询全部
     * @param index ：文档在哪存放
     * @return
     */
    public <T> List<Hit<T,Void>> searchAllByIndexType(String index , String type, T o);
    
   // public List<Document> search(List<Criteria> criterias,String index , String type);
    
 }
