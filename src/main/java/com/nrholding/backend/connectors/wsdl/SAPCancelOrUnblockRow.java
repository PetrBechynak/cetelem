
package com.nrholding.backend.connectors.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BAPIRET2", propOrder = {
    "type","id","number","message","logno","logmsgno","messagev1","messagev2","messagev3","messagev4","parameter", "row","field","system"
})

public class SAPCancelOrUnblockRow {
    @XmlElement(name = "TYPE")
    protected String type;
    @XmlElement(name = "ID")
    protected String id;
    @XmlElement(name = "NUMBER")
    protected String number;
    @XmlElement(name = "MESSAGE")
    protected String message;
    @XmlElement(name = "LOG_NO")
    protected String logno;
    @XmlElement(name = "LOG_MSG_NO")
    protected String logmsgno;
    @XmlElement(name = "MESSAGE_V1")
    protected String messagev1;
    @XmlElement(name = "MESSAGE_V2")
    protected String messagev2;
    @XmlElement(name = "MESSAGE_V3")
    protected String messagev3;
    @XmlElement(name = "MESSAGE_V4")
    protected String messagev4;
    @XmlElement(name = "PARAMETER")
    protected String parameter;
    @XmlElement(name = "ROW")
    protected Integer row;
    @XmlElement(name = "FIELD")
    protected String field;
    @XmlElement(name = "SYSTEM")
    protected String system;

    public String getTYPE() {
        return type;
    }

}