package com.nrholding.backend.connectors.wsdl;

import org.apache.log4j.Logger;
import org.ini4j.Wini;

import javax.xml.soap.*;

/**
 * Created by pbechynak on 22.2.2016.
 */
public class SapClient extends SoapClient {
    final static Logger logger = Logger.getLogger(SapClient.class);

    public SapClient(Wini prop) {
        this.password = prop.get("sap", "PASSWD", String.class);
        this.user = prop.get("sap","USER",String.class);
        this.mshost = prop.get("sap","MSHOST",String.class);
        this.ws_port = prop.get("sap","WS_PORT",String.class);
    }

    public SOAPMessage createRequestZ_WEB_PL_CETELEM() {
        MessageFactory messageFactory = null;
        SOAPMessage soapMessage = null;
        try {
            //logger.debug("Creating SOAP request message to SAP");
            messageFactory = MessageFactory.newInstance();
            soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();

            String serverURI = "urn:sap-com:document:sap:rfc:functions";

            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration("urn", serverURI);

        /*
        Constructed SOAP Request Message:
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:sap-com:document:sap:rfc:functions">
         <soapenv:Header/>
            <soapenv:Body>
                <urn:Z_WEB_PL_CETELEM>
                    <ET_PL>
                        <item>	</item>
                    </ET_PL>
                    <IV_NO_Y7>X</IV_NO_Y7>
                </urn:Z_WEB_PL_CETELEM>
            </soapenv:Body>
        </soapenv:Envelope>
         */

            // SOAP Body
            SOAPBody soapBody = envelope.getBody();
            SOAPElement soapBodyElem = soapBody.addChildElement("Z_WEB_PL_CETELEM", "urn");
            SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("ET_PL");
            SOAPElement soapBodyElem2 = soapBodyElem1.addChildElement("item");
            soapBodyElem2.addTextNode("\t");
            SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("IV_NO_Y7");
            soapBodyElem3.addTextNode("X");

            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", "");

            soapMessage.saveChanges();

        /* Print the request message */
            //System.out.print("Request SOAP Message = ");
            //soapMessage.writeTo(System.out);
            //System.out.println();
        } catch (Exception e) {
            logger.error(e);
            //e.printStackTrace();
            hipchat.send("Error in cetelem loan: " + e.getMessage(),"red");
        }
        return soapMessage;
    }

    public SOAPMessage createRequestUnblock(String orderNumber) {
        MessageFactory messageFactory = null;
        SOAPMessage soapMessage = null;

        try {
            messageFactory = MessageFactory.newInstance();
            soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();

            String serverURI = "urn:sap-com:document:sap:rfc:functions";

            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration("urn", serverURI);

        /*
        Constructed SOAP Request Message:
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:sap-com:document:sap:rfc:functions">
        <soapenv:Header/>
          <soapenv:Body>
              <urn:Y_RFC_SALO_SET_PMTSTAT>
                <EX_RET2>
                    <item>  </item>
                </EX_RET2>
                <IX_OZP></IX_OZP>
                <I_CONF_CANCEL></I_CONF_CANCEL>
                <I_CONF_TYPE_1>X</I_CONF_TYPE_1>
                <I_CONF_TYPE_2></I_CONF_TYPE_2>
                <I_XBLNR></I_XBLNR>
                <I_XCOMMIT>X</I_XCOMMIT>
             </urn:Y_RFC_SALO_SET_PMTSTAT>
            </soapenv:Body>
         </soapenv:Envelope>
         */

            // SOAP Body
            SOAPBody soapBody = envelope.getBody();
            SOAPElement soapBodyElem = soapBody.addChildElement("Y_RFC_SALO_SET_PMTSTAT", "urn");
            SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("EX_RET2");
            SOAPElement soapBodyElem2 = soapBodyElem1.addChildElement("item");
            soapBodyElem2.addTextNode("\t");
            SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("IX_OZP");
            SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("I_CONF_CANCEL");
            SOAPElement soapBodyElem5 = soapBodyElem.addChildElement("I_CONF_TYPE_1");
            soapBodyElem5.addTextNode("X");
            SOAPElement soapBodyElem6 = soapBodyElem.addChildElement("I_CONF_TYPE_2");
            SOAPElement soapBodyElem7 = soapBodyElem.addChildElement("I_XBLNR");
            soapBodyElem5.addTextNode(orderNumber);
            SOAPElement soapBodyElem8 = soapBodyElem.addChildElement("I_XCOMMIT");
            soapBodyElem8.addTextNode("X");


            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", "");

            soapMessage.saveChanges();
        } catch (SOAPException e) {
            e.printStackTrace();
        }



        /* Print the request message */
            //System.out.print("Request SOAP Message = ");
            //soapMessage.writeTo(System.out);
            //System.out.println();

        return soapMessage;
    }

    public SOAPMessage createRequestCancel(String orderNumber) {
        MessageFactory messageFactory;
        SOAPMessage soapMessage=null;
        //logger.debug("Creating SOAP request message to SAP");
        try {
            messageFactory = MessageFactory.newInstance();
            soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();

            String serverURI = "urn:sap-com:document:sap:rfc:functions";

            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration("urn", serverURI);

        /*
        Constructed SOAP Request Message:
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:sap-com:document:sap:rfc:functions">
        <soapenv:Header/>
          <soapenv:Body>
              <urn:Y_RFC_SALO_SET_PMTSTAT>
                <EX_RET2>
                    <item>  </item>
                </EX_RET2>
                <IX_OZP></IX_OZP>
                <I_CONF_CANCEL>X</I_CONF_CANCEL>
                <I_CONF_TYPE_1></I_CONF_TYPE_1>
                <I_CONF_TYPE_2></I_CONF_TYPE_2>
                <I_XBLNR></I_XBLNR>
                <I_XCOMMIT>X</I_XCOMMIT>
             </urn:Y_RFC_SALO_SET_PMTSTAT>
            </soapenv:Body>
         </soapenv:Envelope>
         */

            // SOAP Body
            SOAPBody soapBody = envelope.getBody();
            SOAPElement soapBodyElem = soapBody.addChildElement("Y_RFC_SALO_SET_PMTSTAT", "urn");
            SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("EX_RET2");
            SOAPElement soapBodyElem2 = soapBodyElem1.addChildElement("item");
            soapBodyElem2.addTextNode("\t");
            SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("IX_OZP");
            SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("I_CONF_CANCEL");
            soapBodyElem4.addTextNode("X");
            SOAPElement soapBodyElem5 = soapBodyElem.addChildElement("I_CONF_TYPE_1");
            SOAPElement soapBodyElem6 = soapBodyElem.addChildElement("I_CONF_TYPE_2");
            SOAPElement soapBodyElem7 = soapBodyElem.addChildElement("I_XBLNR");
            soapBodyElem5.addTextNode(orderNumber);
            SOAPElement soapBodyElem8 = soapBodyElem.addChildElement("I_XCOMMIT");
            soapBodyElem8.addTextNode("X");


            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", "");

            soapMessage.saveChanges();

        /* Print the request message */
            //System.out.print("Request SOAP Message = ");
            //soapMessage.writeTo(System.out);
            //System.out.println();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return soapMessage;
    }
}
