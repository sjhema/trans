package com.primovision.lutransport.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtil {
	public static  void closeInputStream(InputStream is) {
		if (is == null) {
			return;
		}
		
		try {
			is.close();
			is = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeOutputStream(OutputStream os) {
		if (os == null) {
			return;
		}
		
		try {
			os.close();
			os = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}