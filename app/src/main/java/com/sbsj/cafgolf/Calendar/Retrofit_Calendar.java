package com.sbsj.cafgolf.Calendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class Retrofit_Calendar {

	private static Retrofit retrofit_cal = null;

	public static Retrofit getRetrofit_cal() {
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

		retrofit_cal = new Retrofit.Builder()
				.baseUrl("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/")
				.addConverterFactory(SimpleXmlConverterFactory.create())
				.client(client)
				.build();

		return retrofit_cal;
	}

	public static String makeURL(int year, int month) {
		String month_string = Integer.toString(month);
		if(month < 10) {
			month_string = "0"+month_string;
		}
		String baseURL = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?solYear=";
		String middleURL = "&solMonth=";
		String authKey = "&ServiceKey=XkE2ARCKcCzItWKsP3x4mzJ6CZAsgCc3Dmil6RkI%2BoqmiKyJneu1SdlbeeFzKE4M0S1EhnWoGPcEZAhWi9E6Iw%3D%3D";

		String URL = baseURL+Integer.toString(year)+middleURL+month_string+authKey;

		return URL;
	}
}
