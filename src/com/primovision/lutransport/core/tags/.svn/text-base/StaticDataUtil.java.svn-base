package  com.primovision.lutransport.core.tags;

import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.web.SpringAppContext;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;


public class StaticDataUtil {

	public static String getText(String dataType, String dataValue) {
		Cache cache = (Cache)SpringAppContext.getBean("staticDataCache");
		Element element = cache.get(dataType+"_"+dataValue);
		if(element != null)
		{
			StaticData staticData=(StaticData)element.getValue();
			return staticData.getDataText();
		}
		else {
			return dataValue;
		}
	}
}
