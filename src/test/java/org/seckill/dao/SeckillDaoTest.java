package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by think on 2016-05-26-0026.
 * 配置spring和junit整合，junit启动时加载SpringIOC容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() throws Exception {
        Date killTime = new Date();
        int result = seckillDao.reduceNumber(1000L, killTime);
        System.out.println("result = " + result);
    }

    @Test
    public void queryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void queryAll() throws Exception {
        int offset = 1;
        int limit = 2;
        List<Seckill> seckillList = seckillDao.queryAll(offset,limit);
        for (Seckill s: seckillList
             ) {
            System.out.println(s.getName());
        }
    }
}