package com.nrholding.backend.connectors.wsdl.cetelem;

import com.nrholding.backend.connectors.wsdl.SoapClient;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigDecimal;
import java.net.URL;

/**
 * Created by pbechynak on 24.2.2016.
 */
public class SKCetelemClient extends SoapClient {

   /* public static void main(String[] args) {
        SKCetelemClient skc = new SKCetelemClient();
        Ws_status ws = skc.sendHttps("8012566321","2624229");
        System.out.println(ws.toString());
    }*/

    public Order getOrder(String orderNr, String shopId) {
        Order order = null;
        Ws_status ws_status = sendHttps(orderNr,shopId);//orderNr, shopId);
        if (ws_status!=null && !ws_status.result.equals("error")){
            order = new Order();
            order.setOrderId(orderNr);
            order.setAmountFromCetelem(new BigDecimal(ws_status.getAmount()));
            order.setStatus(ws_status.getState());
            order.setResult(ws_status.getResult());
        }
        return order;
    }

    public SKCetelemClient() {
    }

    @XmlRootElement(name = "ws_status")
    static class Ws_status {
        String result;
        String name;
        String vendor;
        String state;
        String amount;
        String payout;
        String numklient;
        String obj;
        String numaut;
        String text;
        String error;

        @XmlElement
        public void setResult(String result) {
            this.result = result;
        }

        @XmlElement
        public void setName(String name) {
            this.name = name;
        }

        @XmlElement
        public void setVendor(String vendor) {
            this.vendor = vendor;
        }

        @XmlElement
        public void setState(String state) {
            this.state = state;
        }

        public String getAmount() {
            return amount;
        }

        public String getResult() {
            return result;
        }

        public String getState() {
            return state;
        }

        @XmlElement
        public void setAmount(String amount) {
            this.amount = amount;
        }

        @XmlElement
        public void setPayout(String payout) {
            this.payout = payout;
        }

        @XmlElement
        public void setNumklient(String numklient) {
            this.numklient = numklient;
        }

        @XmlElement
        public void setObj(String obj) {
            this.obj = obj;
        }

        @XmlElement
        public void setNumaut(String numaut) {
            this.numaut = numaut;
        }

        @XmlElement
        public void setText(String text) {
            this.text = text;
        }

        @XmlElement
        public void setError(String error) {
            this.error = error;
        }
    }

    private Ws_status sendHttps(String orderNr, String shopId) {
        String httpsURL = "https://www.cetelem.sk/online/ws_status.php";
        String query = "obj="+orderNr+"&vdr=" + shopId;
        Ws_status ws_status = null;

        try {
            URL myurl = new URL(httpsURL);
            HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-length", String.valueOf(query.length()));
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            con.setDoOutput(true);
            con.setDoInput(true);
            DataOutputStream output = new DataOutputStream(con.getOutputStream());
            output.writeBytes(query);
            output.close();
            DataInputStream input = new DataInputStream( con.getInputStream() );

            JAXBContext jaxbContext = JAXBContext.newInstance(Ws_status.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ws_status = (Ws_status) jaxbUnmarshaller.unmarshal(input);

            input.close();
        } catch (Exception e) {
            e.printStackTrace();

            ws_status = null;
        }

        return ws_status;
    }
}
