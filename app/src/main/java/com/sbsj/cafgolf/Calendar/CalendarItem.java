package com.sbsj.cafgolf.Calendar;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name="item", strict = false)
public class CalendarItem {
	@Element
	private String dateKind;

	@Element
	private String dateName;

	@Element
	private String isHoliday;

	@Element
	private String locdate;

	@Element
	private String seq;

	public String getDateKind() {
		return dateKind;
	}

	public String getDateName() {
		return dateName;
	}

	public String getIsHoliday() {
		return isHoliday;
	}

	public String getLocdate() {
		return locdate;
	}

	public String getSeq() {
		return seq;
	}

	@Override
	public String toString() {
		return "CalendarItem [dateKind=" + dateKind + ", dateName=" + dateName + ", isHoliday=" + isHoliday	+ ", locdate=" + locdate + ", locdate=" + locdate + "]";
	}
}
