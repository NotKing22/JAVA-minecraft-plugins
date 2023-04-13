package me.zsnow.redestone.cache;

import java.util.List;
import java.util.Map;

public class MessageConfig {

    private static Map<String, List<String>> config_list;

    public static List<String> getMessageList(String config) {

        List<String> stringList = config_list.getOrDefault(config, null);

        if(stringList == null) {
            stringList = me.zsnow.redestone.config.Configs.config.getConfig().getStringList(config);
            config_list.put(config, stringList);
        }

        return stringList;
    }
}
