package com.primovision.lutransport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.primovision.lutransport.core.cache.Cacheable;

/**
 * 
 */
@Entity
@Table(name = "static_data")
@SuppressWarnings("serial")
public class StaticData extends AbstractBaseModel implements Cacheable {

    /**
     * Attribute dataType.
     */
    @Column(name = "data_type", length = 50)
    private String dataType;

    /**
     * Attribute dataText.
     */
    @Column(name = "data_text", length = 100)
    private String dataText;

    /**
     * Attribute dataValue.
     */
    @Column(name = "data_value", length = 100)
    private String dataValue;

    /**
     * <p> </p>
     * 
     * @return dataType
     */
    public String getDataType() {
	return dataType;
    }

    /**
     * @param dataType
     *            new value for dataType
     */
    public void setDataType(String dataType) {
	this.dataType = dataType;
    }

    /**
     * <p> </p>
     * 
     * @return dataText
     */
    public String getDataText() {
	return dataText;
    }

    /**
     * @param dataText
     *            new value for dataText
     */
    public void setDataText(String dataText) {
	this.dataText = dataText;
    }

    /**
     * <p> </p>
     * 
     * @return dataValue
     */
    public String getDataValue() {
	return dataValue;
    }

    /**
     * @param dataValue
     *            new value for dataValue
     */
    public void setDataValue(String dataValue) {
	this.dataValue = dataValue;
    }

    @Override
    @Transient
    public String getKey() {
	return dataType + "_" + dataValue;
    }

    @Override
    public String getValue() {
	return dataText;
    }
}