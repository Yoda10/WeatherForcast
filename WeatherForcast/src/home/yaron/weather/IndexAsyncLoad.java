package home.yaron.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.SortedSet;
import java.util.TreeSet;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

public class IndexAsyncLoad extends AsyncTask<Void, Void, SortedSet<String>>
{
	final static String TAG = IndexAsyncLoad.class.getSimpleName();
	final static String INDEX_FILE = "AutocompleteIndex.txt";	

	private Context context = null;

	public IndexAsyncLoad(Context context)
	{
		//super();
		this.context = context;
	}	

	@Override
	protected SortedSet<String> doInBackground(Void... params)
	{	
		return loadIndexFromFile(context);		
	}

	@Override
	protected void onPostExecute(SortedSet<String> citiesSet)
	{			
		//final AutoCompleteTextView autoComplete = (AutoCompleteTextView)findViewById(R.id.auto_complete_text);
		//final AutocompleteAdapter adapter = new AutocompleteAdapter(context, android.R.layout.simple_list_item_1, citiesSet);					
		//autoComplete.setAdapter(adapter);
		Log.d(TAG,"onPostExecute(..)");
	}

	private SortedSet<String> loadIndexFromFile(Context context)
	{
		Log.d(TAG, "loadIndexFromFile(..)");	

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

		Log.d(TAG, "Successfully loading cities index from file.");	

		return citiesSet;
	}
}

