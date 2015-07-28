package com.duowan.common.crud;

import com.duowan.leopard.data.jdbc.Jdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class MysqlHerper {
    private static final Logger log = LoggerFactory.getLogger(MysqlHerper.class);

    private Jdbc jdbc;
    private String schemaName;//数据库名称

    public MysqlHerper(Jdbc jdbc) {
        try {
            this.jdbc = jdbc;
            schemaName = jdbc.getDataSource().getConnection().getCatalog();
        } catch (Exception e) {
            log.warn("出现异常：", e);
        }
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
}
