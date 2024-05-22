package net.eight.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * 字符串工具
 *
 * @author  
 * @date 2024/04/22
 */
public final class StringUtils {
    private StringUtils() {
    }

    /**
     * 字符串json数组转Long类型set集合
     *
     * @param jsonList jsonList
     * @return Set<Long>
     */
    public static Set<Long> stringJsonListToLongSet(String jsonList) {
        Set<Long> set = new Gson().fromJson(jsonList, new TypeToken<Set<Long>>() {
        }.getType());
        return Optional.ofNullable(set).orElse(new HashSet<>());
    }
}
