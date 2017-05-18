package com.siebre.test.base;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/applicationContext-bean.xml",
        "classpath:spring/applicationContext-jdbc.xml",
        "classpath:spring/applicationContext-rabbit.xml",
        "classpath:spring/applicationContext-redis.xml",
})
public class DbTestConfig {

}
