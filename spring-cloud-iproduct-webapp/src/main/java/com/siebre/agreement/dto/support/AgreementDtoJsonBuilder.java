package com.siebre.agreement.dto.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siebre.agreement.dto.AgreementDto;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * Created by huangfei on 2017/06/28.
 */
public class AgreementDtoJsonBuilder extends AgreementDtoBuilder {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(AgreementDtoJsonBuilder.class);

    private static final String ROLES_PROPERTY_NAME = "roles";

    private static final String COMPONENTS_PROPERTY_NAME = "components";

    private static final String SPEC_CODE = "specCode";

    private static final List<String> COMPLEX_PROPERTY_NAMES = Arrays.asList(ROLES_PROPERTY_NAME, COMPONENTS_PROPERTY_NAME);

    private Stack<ActualDtoBuilder> builders = new Stack<ActualDtoBuilder>();

    protected AgreementDtoJsonBuilder(Class<? extends AgreementDto> type, String specCode) {
        super(type, specCode);
    }

    public AgreementDto build(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> agreementDtoMap = mapper.readValue(jsonString, HashMap.class);

        buildComponent(agreementDtoMap);

        return super.build();
    }

    protected void buildComponent(Map<String, Object> componentMap) {
        enterComponentBinding(componentMap);

        componentMap.forEach((key, value) -> {
            if (COMPLEX_PROPERTY_NAMES.contains(key)) {
                if (ROLES_PROPERTY_NAME.equals(key)) {
                    ((List<Map<String, Object>>) value).parallelStream().forEach(roleMap -> buildRole(roleMap));
                } else if (COMPLEX_PROPERTY_NAMES.equals(key)) {
                    ((List<Map<String, Object>>) value).parallelStream().forEach(subComponentMap -> buildComponent(subComponentMap));
                }
            } else {
                property(key, value);
            }
        });

        exitComponentBinding(componentMap);
    }

    private void enterComponentBinding(Map<String, Object> componentMap) {
        ActualDtoBuilder builder = null;
        if (builders.empty()) {
            builder = this;
        } else {
            ComponentDtoBuilder componentBuilder = DtoBuilders.componentOf((String) componentMap.get(SPEC_CODE));
            builder = componentBuilder;

            ActualDtoBuilder parentBuilder = builders.peek();
            if (parentBuilder instanceof ComponentDtoBuilderSupport) {
                ((ComponentDtoBuilderSupport) parentBuilder).components(componentBuilder);
            } else {
                log.warn("Unsupported structure.");
                //TODO enhance the error message.
            }
        }

        builders.push(builder);
    }

    private void exitComponentBinding(Map<String, Object> componentMap) {
        builders.pop();
    }

    /**
     * No child component beneath role.
     * @param roleMap
     */
    protected void buildRole(Map<String, Object> roleMap) {
        roleMap.forEach((key, value) -> property(key, value));
    }

}
