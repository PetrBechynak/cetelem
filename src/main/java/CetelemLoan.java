import com.nrholding.backend.connectors.common.Hipchat;
import com.nrholding.backend.connectors.common.Summary;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by pbechynak on 22.2.2016.
 * TODO:
 * - Slovak Cetelem (Oracle query) is not coded at all
 * - Czech SAP has never returned a confirming message
 * - Czech Cetelem is faked in module fakecetelem
 * - SAP amount is hard coded
 * - dodelat logovani pro kibanu OK
 * - dodelat komunikaci s Hipchatem OK
 *
 */
public class CetelemLoan {
    final static Logger logger = Logger.getLogger(CetelemLoan.class);
    Hipchat hipchat = new Hipchat("PetrTestRoom");
    Summary summary = new Summary();

    public CetelemLoan() throws JAXBException, SOAPException {
        start();
    }

    public static void main(String[] args) throws JAXBException, SOAPException {
        new CetelemLoan();
    }

    public void start() {
        logger.debug("Cetelem Loan started...");
        WaitingOrdersStack stack;

            stack = new WaitingOrdersStack();
            Integer NTHREDS=20;
            ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);

            List<Future<Summary>> list = new ArrayList<Future<Summary>>();
            for (int i = 0; i < 1; i++) {
                Callable<Summary> worker = new WaitingOrdersProcessor(stack);
                Future<Summary> submit = executor.submit(worker);
                list.add(submit);
            }
            // now retrieve the result
            for (Future<Summary> future : list) {
                try {
                    summary = summary.sum(future.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    logger.error(e);
                    hipchat.send("Error in Cetelem Loan:" + e.getMessage(),"red");
                 } catch (ExecutionException e) {
                    e.printStackTrace();
                    //logger.error(e);
                    hipchat.send("Error in Cetelem Loan:" + e.getMessage(),"red");
                }
            }
            logger.debug("Confirmed: "+summary.getConfirmed()+", mismatched: "+summary.getMismatched()+", other: "+summary.getOther()+" rejected:" +
                    +summary.getRejected()+", skipped: "+summary.getSkipped()+", unknown:"+summary.getUnknown());
            logger.debug("All threads ended.");



    }
}

