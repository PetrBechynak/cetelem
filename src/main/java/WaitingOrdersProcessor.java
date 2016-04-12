import com.nrholding.backend.connectors.common.Hipchat;
import com.nrholding.backend.connectors.common.Summary;
import com.nrholding.backend.connectors.wsdl.OrderInSAP;
import com.nrholding.backend.connectors.wsdl.cetelem.CZCetelemClient;
import com.nrholding.backend.connectors.wsdl.cetelem.Order;
import com.nrholding.backend.connectors.wsdl.cetelem.SKCetelemClient;
import org.apache.log4j.Logger;

import java.math.MathContext;
import java.util.concurrent.Callable;

/**
 * Created by pbechynak on 26.2.2016.
 */
public class WaitingOrdersProcessor implements Callable {//Runnable {
    final Logger logger = Logger.getLogger(WaitingOrdersProcessor.class);
    Hipchat hipchat = new Hipchat("PetrTestRoom");
    WaitingOrdersStack stack;
    Summary summary = new Summary();
    WaitingOrdersProcessor(WaitingOrdersStack stack){
        this.stack = stack;
    }

    public Summary call(){ //run(){
        logger.debug("Running thread " + Thread.currentThread().getName());
        OrderInSAP orderInSAP;
        orderInSAP = stack.takeOne();
        while (orderInSAP != null){
            processOrder(orderInSAP);
            orderInSAP = stack.takeOne();
        }

        logger.debug("Thread "+ Thread.currentThread().getName() + " has ended.");
        return summary;
    }

    public Order askCetelemAbout(OrderInSAP waitingOrder){
        Order order = null;
        if(waitingOrder.isCzechOrder()) {
            CZCetelemClient cetelem = new CZCetelemClient();
            order = cetelem.getResponseAsOrder("123456");
        } else if (waitingOrder.isSlovakOrder()) {
            SKCetelemClient skCetelemClient = new SKCetelemClient();
            order = skCetelemClient.getOrder(waitingOrder.getBSTKD(),"2624229");
        } else {
            logger.error("Unknown country");
            hipchat.send("Cetelem loan error - Unknown country","red");
        }
        return order;
    }

    public void processOrder(OrderInSAP waitingOrder) {
        Order order = askCetelemAbout(waitingOrder);

        if (order==null) {
            logger.debug("Thread " + Thread.currentThread().getName() + " - Failed to get info from CZ or SK Cetelem. Order nr." + waitingOrder.getBSTKD() + ".");
            summary.incSkipped();
        } else if (order.getResult().equals("error")){
            logger.debug("Thread "+Thread.currentThread().getName()+ " - SK Cetelem returned error. Order nr." + waitingOrder.getBSTKD() + ".");
            summary.incSkipped();
        } else if (order.isAccepted()){
            if(order.getAmountFromCetelem().round(new MathContext(2)).equals(order.getAmountFromSap().round(new MathContext(2)))){
                // Jak to otestovat?:
                if (order.tryUnblock()) {
                    logger.debug("Thread "+Thread.currentThread().getName()+ " - Order nr." + order.getOrderId() + " was approved.");
                    summary.incConfirmed();
                } else {
                    logger.warn("Thread "+Thread.currentThread().getName()+ " - Order nr." + order.getOrderId() + " was approved, but SAP refused to unblock it. Skipped.");
                    summary.incSkipped();
                }
            } else {
                logger.warn("Thread "+Thread.currentThread().getName()+ " - Order nr." + order.getOrderId() + ": payment value mismatch, SAP expects: " + order.getAmountFromSap() + ", Cetelem returns " + order.getAmountFromCetelem());
                summary.incMismatched();
            }
        } else if (order.isDeclined()) {
            if (order.tryCancel()) {
                    logger.debug("Thread "+Thread.currentThread().getName()+ " - Order nr." + order.getOrderId() + " was declined, message to SAP sucessfully sent.");
                    summary.incRejected();
                } else {
                    logger.warn("Thread "+Thread.currentThread().getName()+ " - Order nr." + order.getOrderId() + " was declined, but SAP refused to cancel it. Skipped.");
                    summary.incSkipped();
                }
        } else if (order.isOtherStatus()) {
            logger.debug("Thread "+Thread.currentThread().getName()+ " - Order nr." + order.getOrderId() +" has 'OTHER' status and was skipped.");
            summary.incOther();
        } else if (order.isWaitingStatus()) {
            logger.debug("Thread "+Thread.currentThread().getName()+ " - Order nr." + order.getOrderId() +" is still being approved and was skipped.");
            summary.incSkipped();
        } else {
            logger.warn("Thread "+Thread.currentThread().getName()+ " - Order nr." + order.getOrderId() +" received unknown order status: \"" + order.getResult() + "\"");
            summary.incUnknown();
        }
    }

    /*SELECT *
    FROM IMWTNEW.shop_config
    where option_name='cetelem_seller_code'*/
}
