package com.siebre.agreement.dto.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siebre.agreement.dto.AgreementDto;
import com.siebre.agreement.dto.ComponentData;
import com.siebre.agreement.dto.annotation.support.AnnotatedDto;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created by huangfei on 2017/06/30.
 */
@Test
public class AgreementDtoJsonBuilderTest {

    private String request;

    private static String REQUEST_SAMPLE = "request/sample/appliction-request.json";

    @BeforeTest
    public void init() throws IOException {
        URL resource = this.getClass().getClassLoader().getResource(REQUEST_SAMPLE);
        System.out.print("Read: " + resource.getPath());
        File requestSample =  new File(resource.getFile());

        request = IOUtils.toString(new FileInputStream(requestSample));
    }

    public void testUnmarshal() throws IOException {
//        AgreementDtoJsonBuilder builder = SiebreCloundDtoBuilders.agreementOfJson(request);
//        AgreementDto agreementDto = builder.build();
//
//        Assert.assertEquals(agreementDto.getSpecCode(), "FJHYXtestjml14");

        ObjectMapper mapper = new ObjectMapper();
        ComponentData<String, Object> componentData = mapper.readValue(request, ComponentData.class);
        AgreementDto agreementDto = AnnotatedDto.wrapAs(componentData, AgreementDto.class);
    }

}
