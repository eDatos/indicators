package es.gobcan.istac.indicators.core.domain;

import org.apache.commons.lang.builder.ToStringStyle;

import org.fornax.cartridges.sculptor.framework.domain.AbstractDomainObject;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


public class Subject {
    private String id;
    private String title;

    public Subject() {
    }

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Subject) {
            return getKey().equals(((Subject)obj).getKey());
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return getKey().hashCode();
    }
    
    public Object getKey() {
        return getId();
    }
}
