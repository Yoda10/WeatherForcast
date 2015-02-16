package home.yaron.weather;

import java.util.ArrayList;

public class WeatherForcast
{	
	public static final String MAX = "max";	
	public static final String HUMIDITY = "humidity";
	public static final String DATE = "date";
	public static final String DESCRIPTION = "description";

	int code;
	float message;	
	City city;
	byte cnt;
	ArrayList<WList> list;

	public class City
	{
		int id;
		String name;
		String country;
		String population;		
		Coord coord;
		Sys sys;

		public class Coord
		{
			float lon;
			float lat;
		}

		public class Sys
		{
			int population; 
		}
	}

	public class WList
	{
		long dt;
		Temp temp;
		float pressure;
		byte humidity;
		ArrayList<Weather> weather;
		float speed;
		byte deg;
		byte clouds;

		public class Temp
		{
			float day;
			float min;
			float max;
			float night;
			float eve;
			float morn;
		}

		public class Weather
		{
			int id;
			String main;
			String description;
			String icon;			
		}
	}	 
}
