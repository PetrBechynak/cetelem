package com.nrholding.backend.connectors.wsdl.cetelem;

import javax.net.ssl.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Created by pbechynak on 8.4.2016.
 */
public class CZCetelemClient {
    public static void main(String[] args) {
        CZCetelemClient cZCetelemClient = new CZCetelemClient();
        cZCetelemClient.getResponse("123456");
    }

    public Order getResponseAsOrder(String orderNr){
        CZCetelemResponse czCetelemResponse = getResponse(orderNr);
        Order order = new Order();
        order.setAmountFromCetelem((new BigDecimal(czCetelemResponse.getAmount())));
        order.setAmountFromSap(null);
        order.setOrderId(czCetelemResponse.getOrderId());
        order.setResult(czCetelemResponse.getText());
        order.setStatus(czCetelemResponse.getState());
        return order;
    }

    public CZCetelemResponse getResponse(String orderNr){
        MessageFactory messageFactory = null;
        SOAPMessage soapMessage = null;
            //logger.debug("Creating SOAP request message to Cetelem");
        try {
            messageFactory = MessageFactory.newInstance();
            soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();

            String serverURI = "https://www.cetelem.cz/cws/services/Apply";

            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration("ser", serverURI);

            /*<soapenv:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ser="http://services.cetelem.cz">
            <soapenv:Header/>
            <soapenv:Body>
            <ser:getParameters soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
            <orderNumber xsi:type="xsd:long">?</orderNumber>
            </ser:getParameters>
            </soapenv:Body>
            </soapenv:Envelope>
            */

            // SOAP Body
            SOAPBody soapBody = envelope.getBody();
            SOAPElement soapBodyElem = soapBody.addChildElement("getParameters", "ser");
            SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("orderNumber");
            soapBodyElem1.addTextNode(orderNr);

            MimeHeaders headers = soapMessage.getMimeHeaders();
            //headers.addHeader("SOAPAction", "http://www.sap.com/Z_WEB_PL_CETELEM");

            soapMessage.saveChanges();
            //Print the request message
            System.out.print("Request SOAP Message = ");
            soapMessage.writeTo(System.out);
            System.out.println();

        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SSLContext sslContext = null;
        KeyStore clientStore = null;
        try {
            clientStore = KeyStore.getInstance("PKCS12");
            clientStore.load(new FileInputStream("C:\\Users\\pbechynak\\Documents\\cetelem_loan_concurrent\\src\\main\\resources\\mykeystore.pkcs12"), "123456".toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(clientStore, "123456".toCharArray());
            KeyManager[] kms = kmf.getKeyManagers();

            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream("C:\\Users\\pbechynak\\Documents\\cetelem_loan_concurrent\\src\\main\\resources\\mytrust"), "123456".toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);
            TrustManager[] tms = tmf.getTrustManagers();

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kms, tms, new SecureRandom());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        URL url = null;
        CZCetelemResponse cZCetelemResponse = null;
        try {
            url = new URL("https://www.cetelem.cz/cws/services/Apply");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            soapMessage.writeTo(out);
            String query = new String(out.toByteArray());

            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-length", String.valueOf(query.length()));
            con.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            con.setRequestProperty("SOAPAction", "");
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            con.setDoOutput(true);
            con.setDoInput(true);
            DataOutputStream output = new DataOutputStream(con.getOutputStream());
            output.writeBytes(query);
            output.close();
            DataInputStream input = new DataInputStream(con.getInputStream());

            XMLInputFactory xif = XMLInputFactory.newFactory();
            StreamSource xml = new StreamSource(input);
            XMLStreamReader xsr = xif.createXMLStreamReader(xml);
            xsr.nextTag();
            while(!xsr.getLocalName().equals("getParametersReturn")) {
                xsr.nextTag();
            }

            JAXBContext jc = JAXBContext.newInstance(CZCetelemResponse.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            JAXBElement<CZCetelemResponse> jAXB_cZCetelemResponse = unmarshaller.unmarshal(xsr, CZCetelemResponse.class);

            cZCetelemResponse = jAXB_cZCetelemResponse.getValue();

            input.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        return cZCetelemResponse;
    }

}
