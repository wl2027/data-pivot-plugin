package com.datapivot.plugin.config.trigger;

import com.datapivot.plugin.context.DataPivotApplication;
import com.datapivot.plugin.entity.DataPivotDatabaseInfo;
import com.datapivot.plugin.i18n.DataPivotBundle;
import com.datapivot.plugin.model.DataPivotTrigger;
import com.datapivot.plugin.tool.DatabaseUtil;
import com.datapivot.plugin.tool.MessageUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseConnectionMapperTrigger implements DataPivotTrigger<DataPivotDatabaseInfo> {
    @Override
    public void load(List<DataPivotDatabaseInfo> dataPivotDatabaseInfoList) {
        Map<String, Connection> databaseConnectionMapper = DataPivotApplication.getInstance().MAPPER.DR_DATABASE_CONNECTION_MAPPER;
        DatabaseUtil.closeConnections();
        databaseConnectionMapper.clear();
        Map<String, Connection> collect = new HashMap<>();
        for (DataPivotDatabaseInfo dataPivotDatabaseInfo : dataPivotDatabaseInfoList) {
            Connection connection = getDatabaseConnection(dataPivotDatabaseInfo);
            if (connection != null) {
                collect.put(dataPivotDatabaseInfo.getDatabaseReference(),connection);
            }
        }
        databaseConnectionMapper.putAll(collect);
    }

    private static Connection getDatabaseConnection(DataPivotDatabaseInfo dataPivotDatabaseInfo) {
        String dbUrl = dataPivotDatabaseInfo.getUrl();
        String dbUserName = dataPivotDatabaseInfo.getUserName();
        String dbPassword = dataPivotDatabaseInfo.getPassword();
        Connection conn = null;
        //dbPassword="root123";
        //dbUrl=dbUrl+"&connectTimeout=2000&socketTimeout=10000";
        try {
            DriverManager.setLoginTimeout(1);
            conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        } catch (Exception e) {
            //连接异常 通知
            MessageUtil.Notice.info(DataPivotBundle.message("data.pivot.notice.connection.null", dataPivotDatabaseInfo.getDatabasePath()));
        }
        return conn;
    }
}
