package com.siebre.payment.billing;

import com.siebre.payment.billing.service.AllinReconcileFileManager;
import com.siebre.payment.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;

/**
 * Created by tianci.huang on 2017/6/28.
 */
@RestController
public class ReconcileController {

    @Autowired
    private AllinReconcileFileManager allinReconcileFileManager;

    @RequestMapping(value = "/test/reconclie", method = RequestMethod.GET)
    public void testReconcile(HttpServletResponse response) {
        Date today = new Date();
        Date start = DateUtil.getDayStart(today);
        Date end = DateUtil.getDayEnd(today);
        File file = allinReconcileFileManager.downloadReconcileFile(start, end);
    }

}
