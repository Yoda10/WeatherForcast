package home.yaron.weather;

import home.yaron.weather_forcast.R;
import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity
{
	public static final String URL_WEATHER_FORCAST = "http://api.openweathermap.org/data/2.5/forecast/daily?q=MyCity&mode=json&units=metric&cnt=10&APPID=aed094804441e1b3db932eb18ff3744f";	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);				
		setContentView(R.layout.activity_main);
	}		
}
