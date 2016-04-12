package com.nrholding.backend.connectors.wsdl.cetelem;

import com.nrholding.backend.connectors.common.Hipchat;
import com.nrholding.backend.connectors.common.Infrastructure;
import com.nrholding.backend.connectors.wsdl.SAPCancelOrUnblockResponse;
import com.nrholding.backend.connectors.wsdl.SapClient;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPMessage;
import java.math.BigDecimal;

/**
 * Created by pbechynak on 19.3.2016.
 */
public class Order {
    final Logger logger = Logger.getLogger(Order.class);
    Hipchat hipchat = new Hipchat("PetrTestRoom");

    String result;
    String status;
    BigDecimal amountFromSap;
    BigDecimal amountFromCetelem;

    public String getStatus() {
        return status;
    }
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public BigDecimal getAmountFromSap() {
        return amountFromSap;
    }

    public BigDecimal getAmountFromCetelem() {
        return amountFromCetelem;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAmountFromSap(BigDecimal amountFromSap) {
        this.amountFromSap = amountFromSap;
    }

    public void setAmountFromCetelem(BigDecimal amountFromCetelem) {
        this.amountFromCetelem = amountFromCetelem;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    String orderId;

    public boolean isAccepted(){
        return (status.equals("OK") || status.equals("OK_HALF") || status.equals("OK_WAIT") || status.equals("0")
                || status.equals("2") || status.equals("5")     || status.equals("7") || status.equals("OK_HALF"));
    }

    public boolean isDeclined() {
        return (status.equals("KO_HARD") || status.equals("3") || status.equals("8"));
    }

    public boolean isOtherStatus() {
        return (status.equals("OTHER"));
    }

    public boolean isWaitingStatus() {
        return (status.equals("KO") || status.equals("1") || status.equals("4")
                || status.equals("6") || status.equals("9"));
    }

    public boolean tryUnblock() {
        SapClient sap = new SapClient(Infrastructure.props);
        boolean unblocked = false;
        SOAPMessage sapSoapRequest = sap.createRequestUnblock(orderId);
        SOAPMessage sapSoapResponse = sap.sendSOAPMessage(sapSoapRequest, "/sap/bc/soap/rfc");
        SAPCancelOrUnblockResponse sapResponse = sapSoapToObject(sapSoapResponse);
        if (!sapResponse.getEXRET2().getItem().get(0).getTYPE().equals("E")){ unblocked=true; }
        return unblocked;
    }

    public boolean tryCancel() {
        SapClient sap = new SapClient(Infrastructure.props);
        boolean unblocked = false;
        SOAPMessage sapSoapRequest = sap.createRequestCancel(orderId);
        SOAPMessage sapSoapResponse = sap.sendSOAPMessage(sapSoapRequest, "/sap/bc/soap/rfc");
        SAPCancelOrUnblockResponse sapResponse = sapSoapToObject(sapSoapResponse);
        if (!sapResponse.getEXRET2().getItem().get(0).getTYPE().equals("E")){
            unblocked=true;
        }
        return unblocked;
    }

    public SAPCancelOrUnblockResponse sapSoapToObject(SOAPMessage msg) {
        SAPCancelOrUnblockResponse obj = null;
        Unmarshaller unmarshaller = null;
        try {
            unmarshaller = JAXBContext.newInstance(SAPCancelOrUnblockResponse.class).createUnmarshaller();
            org.w3c.dom.Document bodyDoc = msg.getSOAPBody().extractContentAsDocument();
            obj = (SAPCancelOrUnblockResponse) unmarshaller.unmarshal(bodyDoc);
        } catch (Exception e) {
            logger.error(e);
            hipchat.send("Error in cetelem loan: " + e.getMessage(),"red");

        }
        return obj;
    }

}
