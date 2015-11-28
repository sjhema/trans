package com.primovision.lutransport.core.util;

import java.math.BigDecimal;

public class MathUtil {
	public static double roundUp(double amount, int numDecimal) {
		double decimal = Math.pow(10,numDecimal);
		double result = Math.round(amount * decimal+0.00000001) / decimal;
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(MathUtil.roundUp(287.534999999999999999997, 2));
	}
}
