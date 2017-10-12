package com.siebre.message.test.base;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/applicationContext-bean.xml",
        "classpath:spring/applicationContext-jdbc.xml",
})
@Rollback(true)
public class DbTestConfig {

}
