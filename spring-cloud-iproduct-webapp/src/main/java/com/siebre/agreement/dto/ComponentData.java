package com.siebre.agreement.dto;


import com.siebre.agreement.dto.annotation.*;
import com.siebre.agreement.dto.annotation.Properties;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by huangfei on 2017/07/03.
 */
public class ComponentData<K,V> extends HashMap<K,V> {

    private static final String ROLES_PROPERTY_NAME = "roles";

    private static final String COMPONENTS_PROPERTY_NAME = "components";

    private static final String SPEC_CODE = "specCode";

    private static final List<String> COMPLEX_PROPERTY_NAMES = Arrays.asList(ROLES_PROPERTY_NAME, COMPONENTS_PROPERTY_NAME);

    private List<PropertyData> properties;

    private List<ComponentData<K, V>> components;

    private List<RoleData<K, V>> roles;

    public ComponentData(Map<K, V> map) {
        this.putAll(map);
    }

    public ComponentData() {
        super();
    }

    @Properties
    public List<PropertyData> getProperties() {
        this.properties =  super.entrySet()
                .parallelStream()
                .filter(entry -> !COMPLEX_PROPERTY_NAMES.contains(entry.getKey()))
                .map(entry -> new PropertyData((String) entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString()))
                .collect(Collectors.toList());
        return properties;
    }

    @SpecCode
    public String getSpecCode() {
        return (String) this.get(SPEC_CODE);
    }

    @Roles
    public List<RoleData<K, V>> getRoles() {
        List<Map<K, V>> roleNodes = (List<Map<K, V>>) super.get(ROLES_PROPERTY_NAME);
        if (roleNodes == null || roleNodes.isEmpty())
            return new ArrayList<RoleData<K, V>>();

        this.roles = roleNodes
                .parallelStream()
                .map(map -> new RoleData<K, V>(map))
                .collect(Collectors.toList());

        return roles;
    }

    @Components
    public List<ComponentData<K, V>> getComponents() {
        List<Map<K, V>> componentNodes = (List<Map<K, V>>) super.get(COMPONENTS_PROPERTY_NAME);
        if (componentNodes == null || componentNodes.isEmpty())
            return new ArrayList<ComponentData<K, V>>();

        this.components =  componentNodes
                .parallelStream()
                .map(map -> new ComponentData<K, V>(map))
                .collect(Collectors.toList());

        return components;
    }
}
