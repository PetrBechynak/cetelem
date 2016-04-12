package com.nrholding.backend.connectors.wsdl;

import com.nrholding.backend.connectors.common.Hipchat;
import org.apache.log4j.Logger;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZWEB_PL", propOrder = {
        "bstkd",
        "kwmeng",
        "vkorg",
        "matnr",
        "lifsk",
        "erdat",
        "vtweg",
        "vbeln",
        "posnr"
})
public class OrderInSAP {
    final static Logger logger = Logger.getLogger(OrderInSAP.class);
    @XmlTransient
    Hipchat hipchat = new Hipchat("PetrTestRoom");

    @XmlElement(name = "BSTKD")
    protected String bstkd;
    @XmlElement(name = "KWMENG")
    protected BigDecimal kwmeng;
    @XmlElement(name = "VKORG")
    protected String vkorg;
    @XmlElement(name = "MATNR")
    protected String matnr;
    @XmlElement(name = "LIFSK")
    protected String lifsk;
    @XmlElement(name = "ERDAT")
    protected String erdat;
    @XmlElement(name = "VTWEG")
    protected String vtweg;
    @XmlElement(name = "VBELN")
    protected String vbeln;
    @XmlElement(name = "POSNR")
    protected String posnr;

    public String getBSTKD() {
        return bstkd;
    }
    public String getVKORG() {
        return vkorg;
    }
    public BigDecimal getKWMENG() {
        return kwmeng;
    }

    public boolean isCzechOrder(){
        return getVKORG().startsWith("CZ");
    }
    public boolean isSlovakOrder(){
        return getVKORG().startsWith("SK");
    }
}

