package com.siebre.agreement.dto.support;

import com.google.common.collect.Lists;
import com.siebre.agreement.dto.ActualDto;
import com.siebre.agreement.dto.PropertyDto;

import java.util.List;

/**
 *
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
 */
public abstract class ActualDtoBuilder {

    //XXX consider using plain map
    private List<PropertyDto> properties = Lists.newArrayList();

    protected void addProperty(String name, Object value) {
        properties.add(new SimplePropertyDto(name, value));
    }

    public abstract ActualDto build();

    protected List<PropertyDto> getProperties() {
        return properties;
    }
}
