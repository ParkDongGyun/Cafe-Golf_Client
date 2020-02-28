package com.sbsj.cafgolf.Calendar;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface CalendarService {
	@GET
	Call<CalendarResponse> getHoliday(@Url String url);
}
