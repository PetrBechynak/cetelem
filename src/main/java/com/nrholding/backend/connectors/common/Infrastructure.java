package com.nrholding.backend.connectors.common;

import org.apache.log4j.Logger;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

/**
 * Created by pbechynak on 25.2.2016.
 */
public class Infrastructure{
    public static Wini props = getInfrastructureProperties(".\\src\\main\\resources");
    final static Logger logger = Logger.getLogger(Infrastructure.class);

    public static Wini getInfrastructureProperties(String path) {
        Wini ini = null;
        try {
            //logger.debug("Loading infrastructure from " + path + "\\infrastructure.ini");
            ini = new Wini(new File(path + "\\infrastructure.ini"));
        } catch (IOException e) {
            logger.error(e);
        }
        return ini;

    }
}
