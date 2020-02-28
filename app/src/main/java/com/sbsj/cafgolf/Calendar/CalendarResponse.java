package com.sbsj.cafgolf.Calendar;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "response", strict = false)
public class CalendarResponse {

	@Element
	public CalendarHeader header;

	@Element
	public CalendarBody body;

	@Override
	public String toString() {
		return "RssFeed [channel = " + header + ", body = " + body + "]";
	}
}
