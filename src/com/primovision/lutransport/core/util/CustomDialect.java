package com.primovision.lutransport.core.util;


import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StringType;


public class CustomDialect extends MySQL5InnoDBDialect {
	public CustomDialect(){
		   super();
		   registerFunction("group_concat", new StandardSQLFunction("group_concat", new StringType()));   		   
	
	}
}
