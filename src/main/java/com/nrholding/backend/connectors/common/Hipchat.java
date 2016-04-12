package com.nrholding.backend.connectors.common;

import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by pbechynak on 8.3.2016.
 */
public class Hipchat {
    final static Logger logger = Logger.getLogger(Hipchat.class);
    String room;

    public Hipchat(String room){
        this.room = room;
    }

    public void send(String message, String color) {
    // Communication with hipchat created here https://nrh.hipchat.com/addons/byo/d76e84d0-894c-4a9a-bb1b-04c6078fe0ca?room=2519435
    // curl -d '{"color":"green","message":"My first notification (yey)","notify":false,"message_format":"text"}' -H 'Content-Type: application/json' https://nrh.hipchat.com/v2/room/2519435/notification?auth_token=H6jXFltdYuUdGpuf867yn0xD0kSgqUgAAJJMNkcX

        try {
            String json = "{\"color\":\""+ color
                    +"\",\"message\":\""+ message
                    +"\",\"notify\":false,\"message_format\":\"text\"}";

            String url = "https://nrh.hipchat.com/v2/room/"+ room + "/notification?auth_token=H6jXFltdYuUdGpuf867yn0xD0kSgqUgAAJJMNkcX";
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");


            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = null;
            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(json);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            logger.error(e);
        }



    }

    }

