package cert;


import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * Created by pbechynak on 28.3.2016.
 */
public class CZCetelemTest {
    public static void main(String[] args) throws Exception {
        verze1();
    }


    public static void verze1() throws Exception {

        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        clientStore.load(new FileInputStream("C:\\Users\\pbechynak\\Documents\\cetelem_loan_concurrent\\src\\main\\resources\\mykeystore.pkcs12"), "123456".toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientStore, "123456".toCharArray());
        KeyManager[] kms = kmf.getKeyManagers();

        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream("C:\\Users\\pbechynak\\Documents\\cetelem_loan_concurrent\\src\\main\\resources\\mytrust"), "123456".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        //TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(trustStore);
        TrustManager[] tms = tmf.getTrustManagers();

        SSLContext sslContext = null;
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kms, tms, new SecureRandom());



        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        URL url = new URL("https://www.cetelem.cz/cws/services/Apply?wsdl");

        HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
        //System.out.println(urlConn.getServerCertificates()[0].getType());
        //System.out.println(urlConn.getServerCertificates()[0].hashCode());
        //System.out.println(urlConn.getServerCertificates()[0].getPublicKey().getAlgorithm());
        //System.out.println(urlConn.getServerCertificates()[0].getPublicKey().getFormat());
        //Scanner c = new Scanner()


        System.out.println("****** Content of the URL ********");
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        String input;

        while ((input = br.readLine()) != null){
            System.out.println(input);
        }
        br.close();



    }

    public static  void verze3() throws Exception {
        class X{
            public X() throws Exception{
                    KeyStore clientStore = KeyStore.getInstance("PKCS12");
                    clientStore.load(new FileInputStream("C:\\Users\\pbechynak\\Documents\\cetelem_loan_concurrent\\src\\main\\resources\\mykeystore.pkcs12"), "123456".toCharArray());

                    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                    kmf.init(clientStore, "123456".toCharArray());
                    KeyManager[] kms = kmf.getKeyManagers();

                    TrustManager[] tms = new TrustManager[] {
                        new X509TrustManager() {
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[0];
                            }
                            public void checkClientTrusted(
                                    java.security.cert.X509Certificate[] certs, String authType) {
                            }
                            public void checkServerTrusted(
                                    java.security.cert.X509Certificate[] certs, String authType) {
                            }
                        }
                };

                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(kms, tms, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                URL url = new URL("https://www.cetelem.cz/cws/services/Apply?wsdl");

                //url = new URL("https://kb.mall.local");


                HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
                urlConn.setSSLSocketFactory(sc.getSocketFactory());
                urlConn.connect();

                //System.out.println(urlConn.getResponseMessage());
                System.out.println(readInputStreamToString(urlConn));
            }

            private String readInputStreamToString(HttpsURLConnection connection) {
                String result = null;
                StringBuffer sb = new StringBuffer();
                InputStream is = null;

                try {

                    is = new BufferedInputStream(connection.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String inputLine = "";
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    result = sb.toString();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    result = null;
                }
                finally {
                    if (is != null) {
                        try {
                            is.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return result;
            }
        }

        X x = new X();
    }

}
