
package com.nrholding.backend.connectors.wsdl;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "Y_RFC_SALO_SET_PMTSTAT")
public class SingleOrderSAPQuery {

    @XmlElement(name = "EX_RET2", required = true)
    protected SingleOrderSAPQuery.EXRET2 exret2;
    @XmlElement(name = "IX_OZP")
    protected String ixozp;
    @XmlElement(name = "I_CONF_CANCEL")
    protected String iconfcancel;
    @XmlElement(name = "I_CONF_TYPE_1")
    protected String iconftype1;
    @XmlElement(name = "I_CONF_TYPE_2")
    protected String iconftype2;
    @XmlElement(name = "I_XBLNR", required = true)
    protected String ixblnr;
    @XmlElement(name = "I_XCOMMIT")
    protected String ixcommit;


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
