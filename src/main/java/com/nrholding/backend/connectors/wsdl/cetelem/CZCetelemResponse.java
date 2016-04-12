package com.nrholding.backend.connectors.wsdl.cetelem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class CZCetelemResponse {

        public float amount;
        public String datum;
        public String error;
        public String name;
        public String numaut;
        public float payout;
        public String state;
        public String surname;
        public String text;
        public String vendor;
        public String orderId;


    public float getAmount() {
        return amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDatum() {
        return datum;
    }

    public String getError() {
        return error;
    }

    public String getName() {
        return name;
    }

    public String getNumaut() {
        return numaut;
    }

    public float getPayout() {
        return payout;
    }

    public String getState() {
        return state;
    }

    public String getSurname() {
        return surname;
    }

    public String getText() {
        return text;
    }

    public String getVendor() {
        return vendor;
    }
}
