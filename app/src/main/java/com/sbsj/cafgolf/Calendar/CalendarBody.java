package com.sbsj.cafgolf.Calendar;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "body", strict = false)
public class CalendarBody {
	@Element
	private CalendarItems items;
	@Element
	private String numOfRows;
	@Element
	private String pageNo;
	@Element
	private String totalCount;

	public CalendarItems getItems() {
		return items;
	}

	public String getNumOfRows() {
		return numOfRows;
	}

	public String getPageNo() {
		return pageNo;
	}

	public String getTotalCount() {
		return totalCount;
	}

	@Override
	public String toString() {
		return "Channel [items = " + items + ", numOfRows=" + numOfRows +  ", pageNo=" + pageNo + ", totalCount=" + totalCount + "]";
	}
}
