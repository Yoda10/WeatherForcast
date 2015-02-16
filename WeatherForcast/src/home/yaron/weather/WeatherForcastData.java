package home.yaron.weather;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Use to deliver the Json parsed data to the UI fragment use. 
 * Including the adapter list data and the UI extra data needed.
 */
public class WeatherForcastData
{
	public static final String HEAT_INDEX = "heatIndex";
	public static final float HEAT_INDEX_ENTRY_TEMPERATURE = 26.7F;

	private List<HashMap<String, Object>> weatherList = null;
	private int average;
	private String cityName;
	private String startDate;
	private String endDate;	

	public List<HashMap<String, Object>> getWeatherList()
	{
		return weatherList;
	}

	public void setWeatherList(List<HashMap<String, Object>> weatherList)
	{
		this.weatherList = weatherList;
	}

	public int getAverage() 
	{
		return average;
	}	

	public String getCityName() 
	{
		return cityName;
	}

	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	public String getStartDate() 
	{
		return startDate;
	}	

	public String getEndDate()
	{
		return endDate;
	}	

	/**
	 * Set dates with format.
	 * The list has to be ordered !	 
	 */
	public void setHeaderDates(List<? extends Map<String, ?>> list)
	{
		// Set start and end dates			
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM",Locale.US);	
		Date dateFrom = (Date)list.get(0).get(WeatherForcast.DATE);
		startDate = dateFormat.format(dateFrom);		
		Date dateTo = (Date)list.get(list.size()-1).get(WeatherForcast.DATE);
		endDate = dateFormat.format(dateTo);
	}

	public void computeAverageTemperature(List<HashMap<String, Object>> list)
	{
		// Calculate average.
		float total = 0;		
		float tmp;

		for( Map<String, ?> listItem : list)
		{
			tmp = (Float)listItem.get(WeatherForcast.MAX);
			total += tmp;
		}

		this.average = Math.round(total / list.size());
	}

	class MapComparator implements Comparator<Map<String, Object>>
	{
		public int compare(Map<String, Object> first, Map<String, Object> second)
		{			
			Date firstValue = (Date)first.get(WeatherForcast.DATE);
			Date secondValue = (Date)second.get(WeatherForcast.DATE);
			return firstValue.compareTo(secondValue);					
		}
	}

	public void orderList(List<HashMap<String, Object>> list)
	{
		Collections.sort(list, new MapComparator());
	}	

	/**
	 * Compute heat index, only from 26.7 celsius degrees.
	 * 
	 * @param rh - relative humidity %
	 * @param F - Fahrenheit temperature
	 * @return heat index
	 */
	public float computeHeatIndex(int rh,float F)
	{
		return (float)(-42.379 + 2.04901523*F + 10.14333127*rh 
				- 0.22475541*F*rh - 6.83783*Math.pow(10,-3)*F*F
				- 5.481717*Math.pow(10,-2)*rh*rh
				+ 1.22874*Math.pow(10,-3)*F*F*rh 
				+ 8.5282*Math.pow(10,-4)*F*rh*rh 
				- 1.99*Math.pow(10,-6)*F*F*rh*rh);		
	}

	/**
	 * Convert F temperature to C temperature.
	 * @param Fahr temperature
	 * @return - Celsius temperature
	 */
	public float convertFtoC(float Fahr)
	{
		return (float)(0.55556 * (Fahr - 32));		
	}

	/**
	 * Convert C temperature to F temperature.
	 * @param Celsius temperature
	 * @return - Fahr temperature
	 */
	public float convertCtoF(float Cels)
	{
		return (float)(1.8 * Cels + 32);		
	}

	public void addHeatIndexToList(List<HashMap<String, Object>> list)
	{
		// Calculate heat index.
		for( Map<String, Object> listItem : list)
		{
			final float tmpC = (Float)listItem.get(WeatherForcast.MAX);

			if( tmpC >= HEAT_INDEX_ENTRY_TEMPERATURE )
			{
				final byte rh = (Byte)listItem.get(WeatherForcast.HUMIDITY);
				final float tmpF = convertCtoF(tmpC);
				final float indxF = computeHeatIndex(rh, tmpF);
				final float indxC = convertFtoC(indxF);			
				listItem.put(WeatherForcastData.HEAT_INDEX,indxC);
			}
		}				
	}
}
