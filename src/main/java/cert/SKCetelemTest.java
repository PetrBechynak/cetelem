package cert;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * Created by pbechynak on 12.3.2016.
 */
public class SKCetelemTest {

    public static void main(String[] args) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, KeyManagementException {
        /*KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream inputstream = new FileInputStream("C:\\Program Files\\Java\\jdk1.7.0_79\\jre\\lib\\security\\MyKeyStore.jks");

        keyStore.load(inputstream, "Ostru6ina".toCharArray());
        inputstream.close();

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, tmf.getTrustManagers(), null);
        SSLSocketFactory sslFactory = ctx.getSocketFactory();

        String urlStr = "https://www.cetelem.sk/online/ws_status.php";
        URL url = new URL(urlStr);
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        //conn.setSSLSocketFactory(sslFactory);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        OutputStreamWriter o = new OutputStreamWriter(conn.getOutputStream());
        o.write("obj=8007870560&vdr=2624229");
        o.flush();*/


        String httpsURL = "https://www.cetelem.sk/online/ws_status.php";
        String query = "obj=8012566321&vdr=2624229";
        URL myurl = new URL(httpsURL);
        HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-length", String.valueOf(query.length()));
        con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
        con.setDoOutput(true);
        con.setDoInput(true);
        DataOutputStream output = new DataOutputStream(con.getOutputStream());
        output.writeBytes(query);
        output.close();
        DataInputStream input = new DataInputStream( con.getInputStream() );
        for( int c = input.read(); c != -1; c = input.read() )
            System.out.print( (char)c );
        input.close();
        System.out.println("Resp Code:"+con .getResponseCode());
        System.out.println("Resp Message:"+ con .getResponseMessage());


        // curl -X POST -k --cacert ca-certificates.crt -d "obj=8007870560&vdr=2624229" https://www.cetelem.sk/online/ws_status.php

        // Blokovane objednavky cetelem:
        // select * from vbak k join vbap p on p.vbeln=k.vbeln where matnr='3000' and lifsk='Y6' and k.erdat > '20160110' and vkorg='SK10'
        // BSTKD v tabulce M_VMVAA, zajima me cislo objednavky a ne vbeln

    }
}
