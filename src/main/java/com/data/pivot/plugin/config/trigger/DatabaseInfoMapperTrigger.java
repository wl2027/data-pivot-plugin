package com.data.pivot.plugin.config.trigger;

import com.data.pivot.plugin.entity.DataPivotDatabaseInfo;
import com.data.pivot.plugin.context.DataPivotApplication;
import com.data.pivot.plugin.model.DataPivotTrigger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DatabaseInfoMapperTrigger implements DataPivotTrigger<DataPivotDatabaseInfo> {
    @Override
    public void load(List<DataPivotDatabaseInfo> dataPivotDatabaseInfoList) {
        //uid->database
        DataPivotApplication.getInstance().MAPPER.DP_DS_DATABASE_MAPPER.clear();
        Map<String, List<String>> collect = dataPivotDatabaseInfoList.stream().collect(
                Collectors.groupingBy(
                        DataPivotDatabaseInfo::getUniqueId,
                        Collectors.mapping(DataPivotDatabaseInfo::getDatabaseName, Collectors.toList())));
        DataPivotApplication.getInstance().MAPPER.DP_DS_DATABASE_MAPPER.putAll(collect);
    }
}
