package com.neo;

import com.neo.model.PddOderBean;
import com.neo.service.SettleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
    @Autowired
    private  SettleService settleService;
    @Test
	public void contextLoads() {
        PddOderBean pd=new PddOderBean();
        pd.setOrder_sn("21321321312312312");
        pd.setP_id("4165519_50161401");
        pd.setPromotion_amount(1000);
        Boolean aBoolean = settleService.pddSettle(pd);
    }

}
