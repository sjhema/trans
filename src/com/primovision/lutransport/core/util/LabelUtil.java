package com.primovision.lutransport.core.util;

import com.primovision.lutransport.core.tags.CacheUtil;

public class LabelUtil {
    public static String getText(String code, String locale) {
	if (locale == null) {
	    locale = "en_US";
	}
	return CacheUtil.getText("messageResourceCache", "label_" + code + "_" + locale);

    }
}
