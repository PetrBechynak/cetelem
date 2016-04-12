import com.nrholding.backend.connectors.common.Hipchat;
import com.nrholding.backend.connectors.common.Infrastructure;
import com.nrholding.backend.connectors.wsdl.SapClient;
import com.nrholding.backend.connectors.wsdl.OrderInSAP;
import com.nrholding.backend.connectors.wsdl.OrderInSAPBulkResponseObject;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pbechynak on 27.2.2016.
 */
public class WaitingOrdersStack extends ArrayList<OrderInSAP> {
    final static Logger logger = Logger.getLogger(WaitingOrdersStack.class);
    Hipchat hipchat = new Hipchat("PetrTestRoom");

    public WaitingOrdersStack() {
        SapClient sap = new SapClient(Infrastructure.props);
        SOAPMessage sapSoapRequest = sap.createRequestZ_WEB_PL_CETELEM();
        try{
            SOAPMessage sapSoapResponse = sap.sendSOAPMessage(sapSoapRequest, "/sap/bc/soap/rfc");
            OrderInSAPBulkResponseObject sapResponse = sapSoapToObject(sapSoapResponse);
            List<OrderInSAP> waitingOrdersRaw = sapResponse.getETPL().getItem();
            for (OrderInSAP waitingOrderRaw: waitingOrdersRaw){
                if (!waitingOrderRaw.getBSTKD().equals("")) {
                    this.add(waitingOrderRaw);
                }
            }

            logger.debug("Sap Z_WEB_PL_CETELEM data loaded: "+ waitingOrdersRaw.size() + " records");
            logger.debug(waitingOrdersRaw.size() - this.size() + " records has missing BSTKD, proceeding with " + this.size());
        } catch (Exception e) {
            logger.error(e);
            final StringWriter sw = new StringWriter();
            try { TransformerFactory.newInstance().newTransformer().transform(new DOMSource(sapSoapRequest.getSOAPPart()),new StreamResult(sw));
            } catch (TransformerException ex) {}
            logger.error("The SOAP message was: " + sw.toString());
            hipchat.send("Error in Cetelem loan: "+e.getMessage(),"red");
        }
    }

    public synchronized OrderInSAP takeOne(){
    OrderInSAP orderInSAP = null;
        if (this.size()>0){
            orderInSAP = this.get(0);
            this.remove(0);
        }
        return orderInSAP;
    }

    public OrderInSAPBulkResponseObject sapSoapToObject(SOAPMessage msg) {
        OrderInSAPBulkResponseObject cet = null;
        Unmarshaller unmarshaller = null;
        try {
            unmarshaller = JAXBContext.newInstance(OrderInSAPBulkResponseObject.class).createUnmarshaller();
            org.w3c.dom.Document bodyDoc = msg.getSOAPBody().extractContentAsDocument();
            cet = (OrderInSAPBulkResponseObject) unmarshaller.unmarshal(bodyDoc);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            hipchat.send("Error in Cetelem loan: "+e.getMessage(),"red");
        }


        return cet;
    }

}
