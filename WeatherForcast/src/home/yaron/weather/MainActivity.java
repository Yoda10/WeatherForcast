package home.yaron.weather;

import home.yaron.weather_forcast.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity
{
	public static final String URL_WEATHER_FORCAST = "http://api.openweathermap.org/data/2.5/forecast/daily?q=MyCity&mode=json&units=metric&cnt=10";	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		setContentView(R.layout.activity_main);
	}		
}
