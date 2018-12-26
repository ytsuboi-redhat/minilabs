package com.redhat.labsjp.sample.todobackend.domain;

import org.junit.Assert;
import org.junit.Test;

public class GenericDomainTest {

    class Doamin extends GenericDomain {

        private static final long serialVersionUID = 844842321030649093L;

        Integer id;

        public Doamin() {
        }

        public Doamin(Integer _id) {
            this.id = _id;
        }

        protected Object keyObject() {
            return id;
        }
    }

    @Test
    public void equalsNull() {
        Doamin domain = new Doamin(0);
        Assert.assertFalse(domain.equals(null));
    }

    @Test
    public void equalsNotGenericDomain() {
        Doamin domain = new Doamin(0);
        Object object = new Object();
        Assert.assertFalse(domain.equals(object));
    }

    @Test
    public void equalsKeyObjectNull() {
        Doamin domain1 = new Doamin();
        Doamin domain2 = new Doamin(1);
        Assert.assertFalse(domain1.equals(domain2));
        Assert.assertFalse(domain2.equals(domain1));
    }

    @Test
    public void equalsNot() {
        Doamin domain1 = new Doamin(0);
        Doamin domain2 = new Doamin(1);
        Assert.assertFalse(domain1.equals(domain2));
        Assert.assertFalse(domain2.equals(domain1));
    }

    @Test
    public void equals() {
        Doamin domain1 = new Doamin(1);
        Doamin domain2 = new Doamin(1);
        Assert.assertTrue(domain1.equals(domain2));
        Assert.assertTrue(domain2.equals(domain1));
    }

    @Test
    public void hashCodeNullNotEqual() {
        Doamin domain1 = new Doamin();
        Doamin domain2 = new Doamin(1);
        Assert.assertNotEquals(domain1.hashCode(), domain2.hashCode());
    }

    @Test
    public void hashCodeNullEqual() {
        Doamin domain1 = new Doamin();
        Doamin domain2 = new Doamin();
        Assert.assertEquals(domain1.hashCode(), domain2.hashCode());
    }

    @Test
    public void hashCodeNotEqual() {
        Doamin domain1 = new Doamin(0);
        Doamin domain2 = new Doamin(1);
        Assert.assertNotEquals(domain1.hashCode(), domain2.hashCode());
    }

    @Test
    public void hashCodeEqual() {
        Doamin domain1 = new Doamin(0);
        Doamin domain2 = new Doamin(0);
        Assert.assertEquals(domain1.hashCode(), domain2.hashCode());
    }

}
