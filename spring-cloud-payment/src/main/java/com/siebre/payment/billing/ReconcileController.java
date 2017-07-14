package com.siebre.payment.billing;

import com.siebre.payment.billing.amqp.RealTimeReconcileProduct;
import com.siebre.payment.billing.base.ReconcileManager;
import com.siebre.payment.billing.service.AlipayReconcileFileManager;
import com.siebre.payment.billing.service.AllinReconcileFileManager;
import com.siebre.payment.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by tianci.huang on 2017/6/28.
 */
@RestController
public class ReconcileController {

    @Autowired
    private AllinReconcileFileManager allinReconcileFileManager;

    @Autowired
    private AlipayReconcileFileManager alipayReconcileFileManager;

    @Autowired
    private ReconcileManager reconcileManager;

    @Autowired
    private RealTimeReconcileProduct realTimeReconcileProduct;

    @RequestMapping(value = "/reconclie/realtime/pay/{orderNumber}", method = RequestMethod.GET)
    public void testRealTime(@PathVariable String orderNumber) {
        realTimeReconcileProduct.sendToRealTimeExchange(orderNumber);
    }

    @RequestMapping(value = "/reconclie/realtime/refund/{orderNumber}", method = RequestMethod.GET)
    public void testRefundRealTime(@PathVariable String orderNumber) {
        realTimeReconcileProduct.sendToRefundRealTimeExchange(orderNumber);
    }

    @RequestMapping(value = "/reconclie/allin/refundjob", method = RequestMethod.GET)
    public void testReconcile(HttpServletResponse response) {
        reconcileManager.runReconJob("allin-pay-realtime-reconcile-job");
    }

    @RequestMapping(value = "/reconclie/allin/downloadfile", method = RequestMethod.GET)
    public void testReconcileAllin(HttpServletResponse response) {
        Date today = new Date();
        Date start = DateUtil.getDayStart(today);
        Date end = DateUtil.getDayEnd(today);
        File file = allinReconcileFileManager.downloadReconcileFile(start, end);
    }

    @RequestMapping(value = "/reconclie/alipay/downloadfile", method = RequestMethod.GET)
    public void testReconcileAlipay(HttpServletResponse response) throws IOException {
        Date today = new Date();
        Date yesterday = DateUtil.addDay(today, -2);
        Date start = DateUtil.getDayStart(yesterday);
        Date end = DateUtil.getDayEnd(yesterday);
        File file = alipayReconcileFileManager.downloadReconcileFile(start, end);
    }

}
