package com.nrholding.backend.connectors.wsdl;

import com.nrholding.backend.connectors.common.Hipchat;
import com.sap.conn.jco.util.Codecs;
import org.apache.log4j.Logger;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by pbechynak on 24.2.2016.
 */
public class SoapClient {
        String password;
        String user;
        public String mshost;
        public String ws_port;
        final static Logger logger = Logger.getLogger(SoapClient.class);
        Hipchat hipchat = new Hipchat("PetrTestRoom");

        public SOAPMessage sendSOAPMessage(SOAPMessage message, String SOAPService) {
            SOAPMessage soapResponse = null;
            //logger.debug("Sending SOAP Message to " + mshost + ":" + ws_port);
            if (user != "") {
                String loginPassword = user + ":" + password;
                message.getMimeHeaders().addHeader("Authorization", "Basic " + new String(Codecs.Base64.encode(loginPassword.getBytes())));
            }

            try {
                // Create SOAP Connection
                SOAPConnectionFactory soapConnectionFactory = null;
                soapConnectionFactory = SOAPConnectionFactory.newInstance();
                SOAPConnection soapConnection = soapConnectionFactory.createConnection();
                // Send SOAP Message to SOAP Server
                String url = "http://" + mshost + ":" + ws_port + SOAPService;
                soapResponse = soapConnection.call(message, url);
                soapConnection.close();
                //soapResponse.writeTo(System.out);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return soapResponse;
        }

    private static void printSOAPResponse(SOAPMessage soapResponse)  {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
            Source sourceContent = soapResponse.getSOAPPart().getContent();
            System.out.print("\nResponse SOAP Message = ");
            StreamResult result = new StreamResult(System.out);
            transformer.transform(sourceContent, result);
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error(e);

        }

    }
}

