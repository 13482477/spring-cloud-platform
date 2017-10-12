package com.siebre.paas.test.sequence;

import com.siebre.paas.Application;
import com.siebre.basic.sequence.SequenceGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by jhonelee on 2017/7/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class SequenceTest {

    @Autowired
    private SequenceGenerator sequenceGenerator;

    @Test
    public void nextValueTest() {
        String value = this.sequenceGenerator.next();
        System.out.println(value);
    }

}
