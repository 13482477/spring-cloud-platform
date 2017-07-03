package com.siebre.agreement.dto.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siebre.agreement.dto.AgreementDto;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * Created by huangfei on 2017/06/28.
 */
public class AgreementDtoJsonBuilder {

    private AgreementDtoBuilder agreementDtoBuilder;

    private static Logger log = org.slf4j.LoggerFactory.getLogger(AgreementDtoJsonBuilder.class);

    private static final String ROLES_PROPERTY_NAME = "roles";

    private static final String COMPONENTS_PROPERTY_NAME = "components";

    private static final String SPEC_CODE = "specCode";

    private static final String KIND = "kind";

    private static final List<String> COMPLEX_PROPERTY_NAMES = Arrays.asList(ROLES_PROPERTY_NAME, COMPONENTS_PROPERTY_NAME);

    private Stack<ActualDtoBuilder> builders = new Stack<ActualDtoBuilder>();

    public AgreementDtoJsonBuilder(AgreementDtoBuilder agreementDtoBuilder) {
        this.agreementDtoBuilder = agreementDtoBuilder;
    }

    public AgreementDto build(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> agreementDtoMap = mapper.readValue(jsonString, HashMap.class);

        buildComponent(agreementDtoMap);

        return agreementDtoBuilder.build();
    }

    public void buildComponent(Map<String, Object> componentMap) {
        enterComponentBinding(componentMap);
        componentMap.forEach((key, value) -> {
            if (COMPLEX_PROPERTY_NAMES.contains(key)) {
                if (ROLES_PROPERTY_NAME.equals(key)) {
                    for(Map<String, Object> roleMap : (List<Map<String, Object>>) value) {
                        buildRole(roleMap);
                    }
                    //((List<Map<String, Object>>) value).parallelStream().forEach(roleMap -> buildRole(roleMap));
                } else if (COMPONENTS_PROPERTY_NAME.equals(key)) {
                    for(Map<String, Object> subComponentMap : (List<Map<String, Object>>) value) {
                        buildComponent(subComponentMap);
                    }
                    //((List<Map<String, Object>>) value).parallelStream().forEach(subComponentMap -> buildComponent(subComponentMap));
                }
            } else {
                ComponentDtoBuilderSupport currentBuilder = (ComponentDtoBuilderSupport) builders.peek();
                currentBuilder.property(key, value);
            }
        });

        exitComponentBinding(componentMap);
    }

    private void enterComponentBinding(Map<String, Object> componentMap) {
        ActualDtoBuilder builder = null;
        if (builders.empty()) {
            builder = agreementDtoBuilder;
        } else {
            //TODO throw an exception here when specCode is null
            ComponentDtoBuilder componentBuilder = DtoBuilders.componentOf((String) componentMap.get(SPEC_CODE));
            builder = componentBuilder;
        }

        builders.push(builder);
    }

    private void exitComponentBinding(Map<String, Object> componentMap) {
        ActualDtoBuilder currentBuilder = builders.pop();
        ComponentDtoBuilder currentComponentBuilder = null;
        if (currentBuilder instanceof ComponentDtoBuilder) {
            currentComponentBuilder = (ComponentDtoBuilder) currentBuilder;
        } else if (currentBuilder instanceof AgreementDtoBuilder) {
            return;
        }

        ComponentDtoBuilderSupport parentBuilder = (ComponentDtoBuilderSupport) builders.peek();
//        if (currentBuilder instanceof ComponentDtoBuilderSupport) {
//            ((ComponentDtoBuilderSupport) currentBuilder).components(componentBuilder);
//        } else {
//            log.warn("Unsupported structure.");
//            //TODO enhance the error message.
//        }
        parentBuilder.components(currentComponentBuilder);

//        builders.pop();
    }

    /**
     * No child component beneath role.
     * @param roleMap
     */
    protected void buildRole(Map<String, Object> roleMap) {
        RoleDtoBuilder roleDtoBuilder = DtoBuilders.roleOf((String) roleMap.get(KIND));
        roleMap.forEach((key, value) -> roleDtoBuilder.property(key, value));

        ComponentDtoBuilderSupport parnetBuilder = (ComponentDtoBuilderSupport) builders.peek();
        parnetBuilder.roles(roleDtoBuilder);
    }

    public AgreementDtoBuilder getAgreementDtoBuilder() {
        return agreementDtoBuilder;
    }

    public void setAgreementDtoBuilder(AgreementDtoBuilder agreementDtoBuilder) {
        this.agreementDtoBuilder = agreementDtoBuilder;
    }

    public Object build() {
        return agreementDtoBuilder.build();
    }
}
