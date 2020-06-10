
package com.haier.hailian.contract.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 *
 */
// 测试
//@WebServiceClient(name = "createchaingroup_client_ep", targetNamespace = "http://xmlns.oracle.com/Interface/CreateChainGroup", wsdlLocation = "http://10.135.16.46:8001/soa-infra/services/interface/CreateChainGroup/createchaingroup_client_ep?WSDL")
// 生产
@WebServiceClient(name = "createchaingroup_client_ep", targetNamespace = "http://xmlns.oracle.com/Interface/CreateChainGroup", wsdlLocation = "http://bpel.mdm.haier.com:7778/soa-infra/services/interface/CreateChainGroup/createchaingroup_client_ep?WSDL")
public class CreatechaingroupClientEp
        extends Service
{

    private final static URL CREATECHAINGROUPCLIENTEP_WSDL_LOCATION;
    private final static WebServiceException CREATECHAINGROUPCLIENTEP_EXCEPTION;
    private final static QName CREATECHAINGROUPCLIENTEP_QNAME = new QName("http://xmlns.oracle.com/Interface/CreateChainGroup", "createchaingroup_client_ep");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            // 测试
            //url = new URL("http://10.135.16.46:8001/soa-infra/services/interface/CreateChainGroup/createchaingroup_client_ep?WSDL");
            // 生产
            url = new URL("http://bpel.mdm.haier.com:7778/soa-infra/services/interface/CreateChainGroup/createchaingroup_client_ep?WSDL");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        CREATECHAINGROUPCLIENTEP_WSDL_LOCATION = url;
        CREATECHAINGROUPCLIENTEP_EXCEPTION = e;
    }

    public CreatechaingroupClientEp() {
        super(__getWsdlLocation(), CREATECHAINGROUPCLIENTEP_QNAME);
    }

    public CreatechaingroupClientEp(WebServiceFeature... features) {
        super(__getWsdlLocation(), CREATECHAINGROUPCLIENTEP_QNAME, features);
    }

    public CreatechaingroupClientEp(URL wsdlLocation) {
        super(wsdlLocation, CREATECHAINGROUPCLIENTEP_QNAME);
    }

    public CreatechaingroupClientEp(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, CREATECHAINGROUPCLIENTEP_QNAME, features);
    }

    public CreatechaingroupClientEp(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CreatechaingroupClientEp(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns CreateChainGroup
     */
    @WebEndpoint(name = "CreateChainGroup_pt")
    public CreateChainGroup getCreateChainGroupPt() {
        return super.getPort(new QName("http://xmlns.oracle.com/Interface/CreateChainGroup", "CreateChainGroup_pt"), CreateChainGroup.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CreateChainGroup
     */
    @WebEndpoint(name = "CreateChainGroup_pt")
    public CreateChainGroup getCreateChainGroupPt(WebServiceFeature... features) {
        return super.getPort(new QName("http://xmlns.oracle.com/Interface/CreateChainGroup", "CreateChainGroup_pt"), CreateChainGroup.class, features);
    }

    private static URL __getWsdlLocation() {
        if (CREATECHAINGROUPCLIENTEP_EXCEPTION!= null) {
            throw CREATECHAINGROUPCLIENTEP_EXCEPTION;
        }
        return CREATECHAINGROUPCLIENTEP_WSDL_LOCATION;
    }

}