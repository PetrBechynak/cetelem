
package com.nrholding.backend.connectors.wsdl;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "Z_WEB_PL_CETELEM.Response",namespace = "urn:sap-com:document:sap:rfc:functions")
public class OrderInSAPBulkResponseObject {

    @XmlElement(name = "ET_PL", required = true)
    protected OrderInSAPBulkResponseObject.ETPL etpl;

    public OrderInSAPBulkResponseObject.ETPL getETPL() {
        return etpl;
    }
    public void setETPL(OrderInSAPBulkResponseObject.ETPL value) {
        this.etpl = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "item"
    })
    public static class ETPL {

        protected List<OrderInSAP> item;
    public List<OrderInSAP> getItem() {
            if (item == null) {
                item = new ArrayList<OrderInSAP>();
            }
            return this.item;
        }

    }

}
