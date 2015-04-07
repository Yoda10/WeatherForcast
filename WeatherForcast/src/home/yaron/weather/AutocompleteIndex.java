package home.yaron.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.SortedSet;
import java.util.TreeSet;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class AutocompleteIndex
{
	final static String TAG = AutocompleteIndex.class.getSimpleName();
	final static String INDEX_FILE = "AutocompleteIndex.txt";

	public static SortedSet<String> loadIndexFromFile(Context context)
	{
		TreeSet<String> citiesSet = new TreeSet<String>();
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;

		try
		{
			// Open stream to cities asset file.
			final AssetManager assetManager = context.getAssets();
			inputStream = assetManager.open(INDEX_FILE);
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));

			String line = null;
			while( (line = bufferedReader.readLine()) != null )
			{
				citiesSet.add(line);
			}
		}
		catch(Exception ex)
		{
			citiesSet = null;
			Log.e(TAG, "Problem loading cities index from file.", ex);			
		}
		finally
		{
			if(bufferedReader == null && inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}

			if(bufferedReader != null)
				try {
					bufferedReader.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}
		}

		Log.e(TAG, "Successfully loading cities index from file.");	

		return citiesSet;
	}
}
