package com.enlightent.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @author jianglei
 * @since 2018/1/30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HiveServiceTest {

    @Autowired
    private HiveService hiveService;

    @Test
    public void executeQuery() throws Exception {
        List<Map> maps = hiveService.executeQuery("SELECT count(*) as con,count(distinct(weixinUnionid)), to_date(browsetime) as tt\n" +
                "FROM\n" +
                "  (SELECT browse_web_info.weixinUnionId,\n" +
                "          browse_web_info.browsetime\n" +
                "   FROM browse_web_info\n" +
                "   LEFT JOIN `user` ON browse_web_info.weixinUnionId = `user`.weixinUnionid\n" +
                "   WHERE (`user`.`userType` IS NULL\n" +
                "          OR `user`.`userType` = 1)\n" +
                "   GROUP BY browse_web_info.weixinUnionid,\n" +
                "            browse_web_info.browseTime) AS VV\n" +
                "group by to_date(browsetime)\n" +
                "order by tt desc");
        System.out.println(maps);
    }

}