package com.siebre.agreement.dto.support;

import com.siebre.agreement.dto.ComponentDto;

/**
 *
 * This is overriding class for Java Classloader. After this file has been added,
 * it will been loaded by Java Classloader prior to class file with the same
 * canonical name in other jars.<p>
 *
 * The reason why we do this is that we can change the dependent type’s behavior
 * by Java language mechanism, for example, type inherit. This file should be
 * removed if dependent type changes.
 *
 * @author ZhangChi
 * @since 2014-3-11
 * @param <T>
 *
 *
 */
public class ComponentDtoBuilder extends ComponentDtoBuilderSupport<ComponentDto> {

    public ComponentDtoBuilder(Class<? extends ComponentDto> type, String specCode) {
        super(type, specCode);
    }

    public ComponentDtoBuilder roles(RoleDtoBuilder... builders) {
        return (ComponentDtoBuilder) super.roles(builders);
    }
}
