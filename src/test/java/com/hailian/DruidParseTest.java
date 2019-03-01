package com.hailian;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import org.junit.Test;

/**
 * Created by long on 2018/11/22.
 */
public class DruidParseTest {
    @Test
    public void testDruid(){
        String testseletSql = "select a.id,b.name from tb a left join tbc b on b.id = a.p_id and a.pid = '1' ";
        String testDelete = "delete from tba where id in (select id from tab_c)";
        String testInsert = "insert into ab (id) values (select 'type',a.id,b.name from tb a left join tbc b on b.id = a.p_id)";
        String testError = " insert into ab (id) values (select id from tab_c)";

        SQLStatementParser parser = new MySqlStatementParser(testInsert);
        SQLStatement statement = parser.parseStatement();
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        statement.accept(visitor);
        // 从visitor中拿出你所关注的信息
        System.out.println(visitor.getColumns());//字段
        System.out.println(visitor.getTables());//表明
        System.out.println(visitor.getRelationships());//关联
        System.out.println( visitor.getConditions());//条件

    }
}
