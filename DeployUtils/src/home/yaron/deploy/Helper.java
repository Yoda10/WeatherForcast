package home.yaron.deploy;

import home.yaron.deploy.CountryList.Country;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;

/**
 * Helper class to parse the Json asset cities file to file used by the cities text view auto complete.
 * The output file is a list of all cities without duplicate cities.	 
 * 
 * @author Yaron Ronen
 * @date 07/04/2015
 */
public class Helper
{
	private static final String TAG = Helper.class.getSimpleName();
	private final static String COUNTRIES_JSON_FILE = "countriesToCities.json";
	private final static String CITIES_DIR = "Yaron";
	private final static String CITIES_FILE = "Cities8.txt";

	/**
	 * parse the Json asset cities file to CountryList object.
	 */
	public static CountryList parseJsonCountriesFile(Context context) //throws IOException
	{	
		int cityCounter = 0;
		CountryList countryList = new CountryList();

		InputStream inputStream = null;
		JsonReader jsonReader = null;

		try
		{
			// Open stream to cities asset file.
			final AssetManager assetManager = context.getAssets();
			inputStream = assetManager.open(COUNTRIES_JSON_FILE);
			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));		
			jsonReader = new JsonReader(bufferedReader);

			jsonReader.beginObject();
			while( jsonReader.hasNext() )
			{
				// Parse country name.				
				final String countryName = jsonReader.nextName();
				countryList.addCountryName(countryName);
				Log.d(TAG,"Country:"+countryName+"\n"+"--------------");

				// Parse cities list from a country.
				jsonReader.beginArray();
				while( jsonReader.hasNext() )
				{
					cityCounter++;
					final String cityName = jsonReader.nextString();
					countryList.addCityName(cityName);
					Log.d(TAG,"counter:"+cityCounter+" city:"+cityName);
				}
				jsonReader.endArray();			
			}
			jsonReader.endObject();	

			Log.d(TAG, "Succsesfuly parsed Json countries list.");
		}
		catch(Exception ex)
		{
			countryList = null;
			Log.e(TAG, "Problems parsing Json countries list.", ex);
		}
		finally
		{	
			if( jsonReader == null && inputStream != null )
				try {
					inputStream.close();
				} catch (IOException e1) {					
					e1.printStackTrace();
				}				

			if( jsonReader != null )
				try {
					jsonReader.close();
				} catch(IOException e2) {					
					e2.printStackTrace();
				}		
		}		

		return countryList;
	}

	/**
	 * Create a cities set.
	 */
	public static Set<String> createCitiesSet(CountryList countryList)
	{
		int counter = 0;
		final TreeSet<String> citiesSet = new TreeSet<String>();

		for( final Country theCountry : countryList.allCountries )
		{
			for( final String theCity : theCountry.cities )
			{
				counter++;
				citiesSet.add(theCity);
			}
		}

		Log.d(TAG, "Cities set created with "+citiesSet.size()+" cities from source of:"+counter+" cities.");

		return citiesSet;
	}

	public static void saveCitiesSetToFile(Set<String> citiesSet)
	{		
		int counter = 0;
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;

		try
		{			
			final File file = new File(Environment.getExternalStoragePublicDirectory(CITIES_DIR),CITIES_FILE);
			final File dir = file.getParentFile();

			if(!dir.exists())
			{
				dir.mkdir();
			}

			if(!file.exists())
			{
				file.createNewFile();
			}

			fileWriter = new FileWriter(file,false);
			bufferedWriter = new BufferedWriter(fileWriter);

			for( String theCity : citiesSet)
			{
				if(isValidCity(theCity))
				{
					counter++;
					bufferedWriter.write(theCity);
					bufferedWriter.newLine();
				}
			}
		}
		catch(Exception ex)
		{
			Log.e(TAG, "Problems writing cities to a file.", ex);
		}
		finally
		{
			if( bufferedWriter != null )
				try {
					bufferedWriter.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}
		}		

		Log.d(TAG, "Write to file "+counter+" cities.");
	}

	private static boolean isValidCity(String city)
	{	
		boolean valid = true;

		if( city.length() == 0 )
			valid = false;

		return valid;
	}
}
