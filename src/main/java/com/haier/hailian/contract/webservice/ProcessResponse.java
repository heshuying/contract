
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
 *         &lt;element name="RETCODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RETMSG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OUT_CHAIN_CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OUT_CHAIN_GROUP_CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "retcode",
    "retmsg",
    "outchaincode",
    "outchaingroupcode"
})
@XmlRootElement(name = "processResponse")
public class ProcessResponse {

    @XmlElement(name = "RETCODE", required = true)
    protected String retcode;
    @XmlElement(name = "RETMSG", required = true)
    protected String retmsg;
    @XmlElement(name = "OUT_CHAIN_CODE", required = true)
    protected String outchaincode;
    @XmlElement(name = "OUT_CHAIN_GROUP_CODE", required = true)
    protected String outchaingroupcode;

    /**
     * ��ȡretcode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRETCODE() {
        return retcode;
    }

    /**
     * ����retcode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRETCODE(String value) {
        this.retcode = value;
    }

    /**
     * ��ȡretmsg���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRETMSG() {
        return retmsg;
    }

    /**
     * ����retmsg���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRETMSG(String value) {
        this.retmsg = value;
    }

    /**
     * ��ȡoutchaincode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOUTCHAINCODE() {
        return outchaincode;
    }

    /**
     * ����outchaincode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOUTCHAINCODE(String value) {
        this.outchaincode = value;
    }

    /**
     * ��ȡoutchaingroupcode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOUTCHAINGROUPCODE() {
        return outchaingroupcode;
    }

    /**
     * ����outchaingroupcode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOUTCHAINGROUPCODE(String value) {
        this.outchaingroupcode = value;
    }

}
