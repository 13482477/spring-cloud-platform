package com.siebre.policy.application.Exception;

import com.google.common.collect.ImmutableList;
import com.siebre.agreement.AgreementException;
import com.siebre.agreement.validation.AgreementValidationError;
import com.siebre.common.base.CollectionStringifier;
import com.siebre.i18n.Messages;

import java.util.List;

/**
 * Created by meilan on 2017/7/4.
 */
public class SiebreCloudRequestExecutionException extends AgreementException {

    private static final CollectionStringifier STRINGIFIER = new CollectionStringifier().itemName("constraint violation");

    private List<AgreementValidationError> errors;

    public SiebreCloudRequestExecutionException(String message) {
        super(message);
    }

    public SiebreCloudRequestExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SiebreCloudRequestExecutionException(List<? extends AgreementValidationError> errors) {
        super(STRINGIFIER.toString(errors));
        this.errors = ImmutableList.copyOf(errors);
    }

    @Messages
    public List<AgreementValidationError> getErrors() {
        return errors;
    }
}
