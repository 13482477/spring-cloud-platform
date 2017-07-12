package com.siebre.agreement.dto.support;

import com.siebre.agreement.Agreement;
import com.siebre.agreement.AgreementRole;
import com.siebre.agreement.dto.ComponentData;
import com.siebre.agreement.dto.RoleData;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * Created by huangfei on 2017/07/10.
 */
public class AgreementConverter {

    private Agreement agreement;

    public AgreementConverter(Agreement agreement) {
        this.agreement = agreement;
    }

    public ComponentData covert() {
        return convertComponent(agreement);
    }

    private ComponentData convertComponent(Agreement agreement) {
        ComponentData component = new ComponentData();
        agreement.getPropertyActuals().
                forEach(propertyActual -> {
                    try {
                        component.put(propertyActual.getKind(), agreement.getSmfProperty(propertyActual.getKind()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        agreement.getRoles().forEach(agreementRole -> component.addRoleData(convertRole(agreementRole)));

        agreement.getComponents().forEach(agreementComponent -> component.addComponentData(convertComponent(agreementComponent)));

        return component;
    }

    private RoleData convertRole(AgreementRole agreementRole) {
        RoleData role = new RoleData();
        agreementRole.getPropertyActuals()
                .forEach(propertyActual -> {
                    try {
                        if (propertyActual.getKind().equals("birthDate") && agreementRole.getSmfProperty(propertyActual.getKind()) != null) {
                            Date birthDate = (Date)agreementRole.getSmfProperty(propertyActual.getKind());
                            String dateStrCheck = DateFormatUtils.format(birthDate, "yyyy-MM-dd HH:mm:ss");
                            role.put(propertyActual.getKind(), dateStrCheck);
                        }else {
                            role.put(propertyActual.getKind(), agreementRole.getSmfProperty(propertyActual.getKind()));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        return role;
    }

}
