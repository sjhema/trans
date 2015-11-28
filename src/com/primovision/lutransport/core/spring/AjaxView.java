package com.primovision.lutransport.core.spring;

import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import flexjson.JSONSerializer;

public class AjaxView extends AbstractView {
    private static Logger log = Logger.getLogger(AjaxView.class.getName());

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
	log.fine("Resolving ajax request view -" + map);
	JSONSerializer serializer = new JSONSerializer();
	String jsonString = serializer.serialize(map);
	response.setContentType("text/plain; charset=UTF-8");
	response.getOutputStream().write(jsonString.getBytes());
    }

}
