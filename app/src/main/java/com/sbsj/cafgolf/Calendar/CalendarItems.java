package com.sbsj.cafgolf.Calendar;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name = "items", strict = false)
public class CalendarItems {
	@ElementList(inline = true, required = false)
	public ArrayList<CalendarItem> item;

	@Override
	public String toString() {
		return "RssFeed [channel=" + item + "]";
	}
}