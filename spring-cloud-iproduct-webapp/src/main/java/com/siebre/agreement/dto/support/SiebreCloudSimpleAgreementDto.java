package com.siebre.agreement.dto.support;

import com.siebre.agreement.dto.ComponentDto;
import com.siebre.agreement.dto.PropertyDto;
import com.siebre.agreement.dto.RoleDto;

import java.util.List;

/**
 * Created by meilan on 2017/7/3.
 */
public class SiebreCloudSimpleAgreementDto extends SimpleAgreementDto {
    public SiebreCloudSimpleAgreementDto() {
        super();
    }

    public SiebreCloudSimpleAgreementDto(String specCode, List<PropertyDto> properties, List<RoleDto> roles, List<ComponentDto> components) {
        super(specCode, properties, roles, components);
    }
}
