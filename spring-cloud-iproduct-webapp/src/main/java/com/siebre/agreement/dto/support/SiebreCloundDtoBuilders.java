package com.siebre.agreement.dto.support;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangfei on 2017/06/29.
 */
public class SiebreCloundDtoBuilders {

    private SiebreCloundDtoBuilders() {}

    public static AgreementDtoJsonBuilder agreementOfJson(String dtoJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> agreementDtoMap = mapper.readValue(dtoJson, HashMap.class);

        AgreementDtoJsonBuilder builder = new AgreementDtoJsonBuilder(SimpleAgreementDto.class, (String) agreementDtoMap.get("specCode"));

        builder.buildComponent(agreementDtoMap);

        return builder;
    }

}
