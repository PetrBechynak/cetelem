
package com.nrholding.backend.connectors.wsdl;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "Y_RFC_SALO_SET_PMTSTAT.Response",namespace = "urn:sap-com:document:sap:rfc:functions")
public class SAPCancelOrUnblockResponse {

    @XmlElement(name = "EX_RET2", required = true)
    protected SAPCancelOrUnblockResponse.EXRET2 exret2;

    public SAPCancelOrUnblockResponse.EXRET2 getEXRET2() {
        return exret2;
    }

    public void setEXRET2(SAPCancelOrUnblockResponse.EXRET2 value) {
        this.exret2 = value;
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "item"
    })


    public static class EXRET2 {

        protected List<SAPCancelOrUnblockRow> item;


        public List<SAPCancelOrUnblockRow> getItem() {
            if (item == null) {
                item = new ArrayList<SAPCancelOrUnblockRow>();
            }
            return this.item;
        }

    }

}
