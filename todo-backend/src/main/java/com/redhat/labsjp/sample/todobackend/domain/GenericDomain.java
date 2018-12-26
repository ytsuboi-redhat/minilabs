package com.redhat.labsjp.sample.todobackend.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public abstract class GenericDomain implements Serializable {

    // ----------------------------------------------------- Protected Methods

    protected abstract Object keyObject();

    // -------------------------------------------------------- Public Methods

    @Override
    public boolean equals(Object object) {
        if (object == null || !getClass().equals(object.getClass())) {
            return false;
        }
        GenericDomain other = (GenericDomain) object;
        return new EqualsBuilder().append(keyObject(), other.keyObject())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(keyObject()).toHashCode();
    }

}