package home.yaron.test;

import home.yaron.test.CountryList.Country;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;

public class Indexer 
{
	private static final String TAG = Indexer.class.getSimpleName();
	private final static String CITIES_FILE = "countriesToCities.json";

	public Map BuildIndex()
	{
		return new HashMap();		
	}

//	public CountryList parseJsonCountriesFile(Context context) throws IOException
//	{	
//		int objectCounter = 0;
//		CountryList countryList = new CountryList();
//
//		final AssetManager assetManager = context.getAssets();
//		final InputStream inputStream = assetManager.open(CITIES_FILE);		
//		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));		
//		final JsonReader jsonReader = new JsonReader(bufferedReader);
//
//		try
//		{
//			jsonReader.beginObject();
//			while( jsonReader.hasNext() )
//			{
//				// Parse country name.
//				objectCounter++;
//				final String countryName = jsonReader.nextName();
//				countryList.addCountryName(countryName);
//				Log.d(TAG,"counter:"+objectCounter+" country:"+countryName);
//
//				// Parse cities list.
//				jsonReader.beginArray();
//				while( jsonReader.hasNext() )
//				{
//					final String cityName = jsonReader.nextString();
//					countryList.addCityName(cityName);
//					Log.d(TAG,"city:"+cityName);
//				}
//				jsonReader.endArray();			
//			}
//			jsonReader.endObject();				
//
//			Log.d(TAG, "Succsesfuly parsed Json countries list.");
//		}
//		catch(Exception ex)
//		{
//			countryList = null;
//			Log.e(TAG, "Problems parsing Json countries list.", ex);
//		}
//		finally
//		{
//			if( jsonReader != null )
//				jsonReader.close();			
//		}		
//
//		return countryList;
//	}	

//	public Set<String> loadAndSortCities(CountryList countryList)
//	{
//		int counter = 0;
//		final TreeSet<String> citiesSet = new TreeSet<String>();
//
//		for( final Country theCountry : countryList.allCountries )
//		{
//			for( final String theCity : theCountry.cities )
//			{
//				counter++;
//				citiesSet.add(theCity);
//			}
//		}
//
//		Log.d(TAG, "Load and sort "+counter+" cities.");
//		
//		return citiesSet;
//	}

//	public void saveCitiesToFile(Set<String> citiesSet) throws IOException
//	{
//		//SortedSet subSet = ((TreeSet)citiesSet).subSet("J", "K");
//		//citiesSet = subSet;
//		
//		int counter = 0;
//		FileWriter fileWriter = null;
//		BufferedWriter bufferedWriter = null;
//
//		try
//		{			
//			final File file = new File(Environment.getExternalStoragePublicDirectory("Yaron"),"Cities3.txt");			
//			if (!file.exists())
//			{				
//				file.createNewFile();
//			}
//
//			fileWriter = new FileWriter(file,false);
//			bufferedWriter = new BufferedWriter(fileWriter);
//
//			for( String theCity : citiesSet)
//			{
//				counter++;
//				bufferedWriter.write(theCity);
//				bufferedWriter.newLine();
//			}
//		}
//		finally
//		{
//			if( bufferedWriter != null )
//				bufferedWriter.close();
//		}		
//
//		Log.d(TAG, "Write to file "+counter+" cities.");
//	}

//	public void sortAndFileCitiesOld(CountryList countryList) throws IOException
//	{
//		int counter = 0;
//		final ArrayList<String> citiesList = new ArrayList<String>(2000);
//
//		for( final Country theCountry : countryList.allCountries )
//		{
//			for( final String theCity : theCountry.cities )
//			{
//				counter++;
//				citiesList.add(theCity);
//			}
//		}
//
//		Log.d(TAG, "Load "+counter+" cities.");
//
//		Collections.sort(citiesList);
//
//		Log.d(TAG, "Sort cities list.");		
//
//		counter = 0;
//		FileWriter fileWriter = null;
//		BufferedWriter bufferedWriter = null;
//
//		try
//		{			
//			File file = new File(Environment.getExternalStoragePublicDirectory("Yaron"),"Cities2.txt");			
//			if (!file.exists())
//			{				
//				file.createNewFile();
//			}
//
//			fileWriter = new FileWriter(file,false);
//			bufferedWriter = new BufferedWriter(fileWriter);
//
//			for( String theCity : citiesList)
//			{
//				counter++;
//				bufferedWriter.write(theCity);
//				bufferedWriter.newLine();
//			}
//		}
//		finally
//		{
//			if( bufferedWriter != null )
//				bufferedWriter.close();
//		}		
//
//		Log.d(TAG, "Write to file "+counter+" cities.");
//	}
}
