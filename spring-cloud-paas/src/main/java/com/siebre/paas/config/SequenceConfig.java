package com.siebre.paas.config;

import com.siebre.basic.sequence.DbBasedSequenceGenerator;
import com.siebre.basic.sequence.SequenceGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jhonelee on 2017/7/27.
 */
@Configuration
@ConditionalOnProperty(prefix = "com.siebre.sequence", name = "enable", havingValue = "true")
public class SequenceConfig {

    @Bean
    public SequenceGenerator sequenceGenerator() {
        DbBasedSequenceGenerator sequenceGenerator = new DbBasedSequenceGenerator();
        return sequenceGenerator;
    }

}
