package home.yaron.weather;

import home.yaron.views.WeatherView;
import home.yaron.weather_forcast.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class WeatherAdapter extends SimpleAdapter 
{
	private Context context;
	private List<? extends Map<String, ?>> weatherList;
	private int average;	

	public WeatherAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to,int average)
	{
		super(context, data, resource, from, to);
		this.context = context;
		this.weatherList = data;
		this.average = average;
	}	

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{		
		if( convertView == null )
		{
			final LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.weather_item, null);
		}

		final float maxTemp = (Float)weatherList.get(position).get(WeatherForcast.MAX);		

		// weather view - position
		final WeatherView weatherView = (WeatherView)convertView.findViewById(home.yaron.weather_forcast.R.id.item_weather_sun);		
		final float scalePosition = (weatherView.getSteps() / 2F) + maxTemp - average; // new
		weatherView.setPosition(Math.round(scalePosition)-1); // new
		weatherView.setTextDrawable(String.valueOf(Math.round(maxTemp)));

		// Day of the week
		final Date date = (Date)weatherList.get(position).get("date");		
		final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE",Locale.US);		
		weatherView.setLeftText(dateFormat.format(date));			

		// weather view - drawable Id		
		setDrawableId(weatherView, maxTemp,position);

		return convertView;
	}	

	private void setDrawableId(WeatherView weatherView, float maxTemp, int position)
	{
		float heatIndex = -1; // Heat index was not computed under entry temperature.
		if( maxTemp >= WeatherForcastData.HEAT_INDEX_ENTRY_TEMPERATURE )
			heatIndex = (Float)weatherList.get(position).get(WeatherForcastData.HEAT_INDEX);	

		if( (maxTemp > 0F && maxTemp < WeatherForcastData.HEAT_INDEX_ENTRY_TEMPERATURE) || (heatIndex >= 20F && heatIndex < 27F) )
		{
			weatherView.setDrawableId(R.drawable.sun);
		}
		else if( maxTemp >= WeatherForcastData.HEAT_INDEX_ENTRY_TEMPERATURE && heatIndex >= 27F && heatIndex < 32F ) // Low
		{
			weatherView.setDrawableId(R.drawable.sun_heat_l1);
		}
		else if( maxTemp >= WeatherForcastData.HEAT_INDEX_ENTRY_TEMPERATURE && heatIndex >= 32F && heatIndex < 41F ) // Medium
		{
			weatherView.setDrawableId(R.drawable.sun_heat_l2);
		}
		else if( maxTemp >= WeatherForcastData.HEAT_INDEX_ENTRY_TEMPERATURE && heatIndex >= 41F && heatIndex < 54F ) // Danger
		{
			weatherView.setDrawableId(R.drawable.sun_heat_l3);
		}
		else if( maxTemp >= WeatherForcastData.HEAT_INDEX_ENTRY_TEMPERATURE && heatIndex >= 54F ) // Extreme danger
		{
			weatherView.setDrawableId(R.drawable.sun_heat_l4);
		}
		else if( maxTemp <= 0F ) // Snow
		{
			weatherView.setDrawableId(R.drawable.snowman);
		}		
		else
		{
			Log.v("Yaron", "Image not found on range.");
		}
	}	
}