
package com.haier.hailian.contract.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IN_CHAIN_CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IN_CHAIN_NAME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IN_MAJOR_CLASS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IN_SUB_CLASS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IN_CHAIN_ATTR" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IN_SHORT_NAME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IN_IS_TPT" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IN_MANAGER" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IN_SUB_CLASS_MANAGER_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IN_CHAIN_MANAGER_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CREATED_BY" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "inchaincode",
    "inchainname",
    "inmajorclass",
    "insubclass",
    "inchainattr",
    "inshortname",
    "inistpt",
    "inmanager",
    "insubclassmanagerno",
    "inchainmanagerno",
    "createdby"
})
@XmlRootElement(name = "process")
public class Process {

    @XmlElement(name = "IN_CHAIN_CODE", required = true)
    protected String inchaincode;
    @XmlElement(name = "IN_CHAIN_NAME", required = true)
    protected String inchainname;
    @XmlElement(name = "IN_MAJOR_CLASS", required = true)
    protected String inmajorclass;
    @XmlElement(name = "IN_SUB_CLASS", required = true)
    protected String insubclass;
    @XmlElement(name = "IN_CHAIN_ATTR", required = true)
    protected String inchainattr;
    @XmlElement(name = "IN_SHORT_NAME", required = true)
    protected String inshortname;
    @XmlElement(name = "IN_IS_TPT", required = true)
    protected String inistpt;
    @XmlElement(name = "IN_MANAGER", required = true)
    protected String inmanager;
    @XmlElement(name = "IN_SUB_CLASS_MANAGER_NO", required = true)
    protected String insubclassmanagerno;
    @XmlElement(name = "IN_CHAIN_MANAGER_NO", required = true)
    protected String inchainmanagerno;
    @XmlElement(name = "CREATED_BY", required = true)
    protected String createdby;

    /**
     * ��ȡinchaincode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINCHAINCODE() {
        return inchaincode;
    }

    /**
     * ����inchaincode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINCHAINCODE(String value) {
        this.inchaincode = value;
    }

    /**
     * ��ȡinchainname���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINCHAINNAME() {
        return inchainname;
    }

    /**
     * ����inchainname���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINCHAINNAME(String value) {
        this.inchainname = value;
    }

    /**
     * ��ȡinmajorclass���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINMAJORCLASS() {
        return inmajorclass;
    }

    /**
     * ����inmajorclass���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINMAJORCLASS(String value) {
        this.inmajorclass = value;
    }

    /**
     * ��ȡinsubclass���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSUBCLASS() {
        return insubclass;
    }

    /**
     * ����insubclass���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSUBCLASS(String value) {
        this.insubclass = value;
    }

    /**
     * ��ȡinchainattr���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINCHAINATTR() {
        return inchainattr;
    }

    /**
     * ����inchainattr���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINCHAINATTR(String value) {
        this.inchainattr = value;
    }

    /**
     * ��ȡinshortname���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSHORTNAME() {
        return inshortname;
    }

    /**
     * ����inshortname���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSHORTNAME(String value) {
        this.inshortname = value;
    }

    /**
     * ��ȡinistpt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINISTPT() {
        return inistpt;
    }

    /**
     * ����inistpt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINISTPT(String value) {
        this.inistpt = value;
    }

    /**
     * ��ȡinmanager���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINMANAGER() {
        return inmanager;
    }

    /**
     * ����inmanager���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINMANAGER(String value) {
        this.inmanager = value;
    }

    /**
     * ��ȡinsubclassmanagerno���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSUBCLASSMANAGERNO() {
        return insubclassmanagerno;
    }

    /**
     * ����insubclassmanagerno���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSUBCLASSMANAGERNO(String value) {
        this.insubclassmanagerno = value;
    }

    /**
     * ��ȡinchainmanagerno���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINCHAINMANAGERNO() {
        return inchainmanagerno;
    }

    /**
     * ����inchainmanagerno���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINCHAINMANAGERNO(String value) {
        this.inchainmanagerno = value;
    }

    /**
     * ��ȡcreatedby���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCREATEDBY() {
        return createdby;
    }

    /**
     * ����createdby���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCREATEDBY(String value) {
        this.createdby = value;
    }

}
