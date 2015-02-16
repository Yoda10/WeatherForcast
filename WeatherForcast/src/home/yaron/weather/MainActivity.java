package home.yaron.weather;

import home.yaron.weather_forcast.R;
import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity
{
	public static final String URL_WEATHER_FORCAST = "http://api.openweathermap.org/data/2.5/forecast/daily?q=MyCity&mode=json&units=metric&cnt=10";	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);				
		setContentView(R.layout.activity_main);
	}		
}
