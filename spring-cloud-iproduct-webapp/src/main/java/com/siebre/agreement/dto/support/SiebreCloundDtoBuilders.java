package com.siebre.agreement.dto.support;

/**
 * Created by huangfei on 2017/06/29.
 */
public class SiebreCloundDtoBuilders {

    private SiebreCloundDtoBuilders() {}

    public static AgreementDtoJsonBuilder agreementOfJson(String specCode) {
        return new AgreementDtoJsonBuilder(SimpleAgreementDto.class, specCode);
    }



}
