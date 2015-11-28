package com.primovision.lutransport.service;

import java.util.Map;

import ar.com.fdvs.dj.domain.CustomExpression;

import com.primovision.lutransport.core.tags.StaticDataUtil;

public class StaticDataExpression implements CustomExpression {
		 private String fieldName;
		 private String dataType;
		 
         public StaticDataExpression(String fieldName, String dataType) {
			super();
			this.fieldName = fieldName;
			this.dataType = dataType;
		}

		public Object evaluate(Map fields, Map variables, Map parameters) {
                 Object staticData = fields.get(fieldName);
                 return StaticDataUtil.getText(dataType, staticData.toString());
         }

         public String getClassName() {
                 return String.class.getName();
         }
}
