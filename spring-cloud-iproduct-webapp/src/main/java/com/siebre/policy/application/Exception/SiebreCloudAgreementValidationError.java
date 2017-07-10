package com.siebre.policy.application.Exception;

import com.siebre.agreement.Agreement;
import com.siebre.common.validation.ValidationError;
import com.siebre.smf.SmfActualElement;

import java.io.Serializable;

/**
 * Created by meilan on 2017/7/4.
 */
public class SiebreCloudAgreementValidationError implements ValidationError, Serializable {
    private Agreement root;

    private SmfActualElement location;

    private String path;

    private String description;

    /**
     *
     * @param location The element caused this error or the nearest ancestor of the error location
     * when location itself can not be represented as an element.
     * @param locationDescriptor An encoded string describing the location.
     * @param root Top level agreement as the context of this validation error.
     */
    public SiebreCloudAgreementValidationError(SmfActualElement location, String locationDescriptor, Agreement root) {
        this.root = root;
        this.path = locationDescriptor;
        this.location = location;
    }

//    public SiebreCloudAgreementValidationError(String locationDescriptor, String description) {
//        this.path = locationDescriptor;
//        this.description = description;
//    }


    @Override
    public Agreement getRoot() {
        return root;
    }

    @Override
    public String getLocationDescriptor() {
        return path;
    }

    public void setRoot(Agreement root) {
        this.root = root;
    }

    public SmfActualElement getLocation() {
        return location;
    }

    public void setLocation(SmfActualElement location) {
        this.location = location;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
