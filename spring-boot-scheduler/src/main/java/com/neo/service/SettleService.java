package com.neo.service;

import com.neo.jsonbean.JdJson.Jdoder;
import com.neo.model.PddOderBean;
import com.neo.model.TboderBean;

public interface SettleService {
    /**
     * 拼多多结算
     *
     * @param pddOderBean
     * @return
     */
    Boolean pddSettle(PddOderBean pddOderBean);

    /**
     * 淘宝客结算
     *
     * @param tboderBean
     * @return
     */
    Boolean taoBaoSettle(TboderBean tboderBean);
    /**
     * 京东结算
     *
     * @param jdoder
     * @return
     */
    Boolean jdSettle(Jdoder jdoder);
}
