package org.hibernate.dialect;

import java.sql.Types;

/**
 * Created by huangfei on 14/06/2017.
 */
public class SiebreMySQL5Dialect extends MySQL5Dialect {

    public SiebreMySQL5Dialect() {
        super();
        registerColumnType(Types.DECIMAL, "decimal($p,$s)");
    }
}
