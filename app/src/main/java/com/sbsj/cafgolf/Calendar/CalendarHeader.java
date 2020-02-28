package com.sbsj.cafgolf.Calendar;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "header", strict = false)
public class CalendarHeader {
	@Element
	private String resultCode;

	@Element
	private String resultMsg;

	public String getResultCode() {
		return resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	@Override
	public String toString() {
		return "CalendarItem [resultCode=" + resultCode + ", resultMsg=" + resultMsg + "]";
	}
}
