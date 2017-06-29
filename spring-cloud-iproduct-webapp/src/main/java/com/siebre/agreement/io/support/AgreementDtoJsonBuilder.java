package com.siebre.agreement.io.support;

import com.siebre.agreement.dto.AgreementDto;
import com.siebre.agreement.dto.support.AgreementDtoBuilder;

/**
 * Created by huangfei on 2017/06/28.
 */
public class AgreementDtoJsonBuilder extends AgreementDtoBuilder {

    protected AgreementDtoJsonBuilder(Class<? extends AgreementDto> type, String specCode) {
        super(type, specCode);
    }

    public AgreementDto build(String jsonString) {
        //TODO build logic
        return super.build();
    }
}
