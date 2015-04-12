package home.yaron.weather;

import home.yaron.weather_forcast.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherListFragment extends Fragment implements OnClickListener
{
	private final static String TAG = WeatherListFragment.class.getSimpleName();

	private ListView listView;
	private View fragmentView = null;
	private WeatherForcastData weatherForcastData;
	private final CountDownLatch waitLatch = new CountDownLatch(1);

	@Override
	public void onCreate(Bundle savedInstanceState)
	{		
		Log.d(TAG,"onCreate(..)");
		super.onCreate(savedInstanceState);		
		new IndexAsyncLoad().execute(waitLatch);
	}	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		// Inflate the layout for this fragment
		fragmentView = inflater.inflate(R.layout.fragment_weather_list, container, false);

		// The fragment UI is inflated - release the wait latch on index async load task.
		waitLatch.countDown();

		listView = (ListView)fragmentView.findViewById(R.id.fragment_weather_listView);
		listView.requestFocus();		

		// Check for wide layout w400dp = layout_h or smaller layout = layout_v.
		View vLayout = fragmentView.findViewById(R.id.fragment_weather_layout_v);
		if( vLayout != null )
		{
			RelativeLayout r1 = (RelativeLayout)fragmentView.findViewById(R.id.fragment_weather_layout_v_r1);
			int h1 = r1.getLayoutParams().height;

			RelativeLayout r2 = (RelativeLayout)fragmentView.findViewById(R.id.fragment_weather_layout_v_r2);
			int h2 = r2.getLayoutParams().height;

			int height = getResources().getDisplayMetrics().heightPixels;
			LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height - h1 - h2));
			listView.setLayoutParams(lp3);
		}	

		// Set list view.
		GsonHelper gsonHelper = new GsonHelper();
		weatherForcastData = gsonHelper.loadWeatherData(getActivity(),GsonHelper.JSON_FILE_NAME); // parse the json string from file.

		if( weatherForcastData != null )
			setListAdapterAndHeader();				

		// Search button.
		Button searchButton = (Button)fragmentView.findViewById(R.id.fragment_weather_search_button);		
		searchButton.setOnClickListener(this);		

		return fragmentView;
	}	

	private void setListAdapterAndHeader()
	{
		if( getActivity() == null || fragmentView == null ||
				weatherForcastData == null || listView == null ) return;

		// ---- Set list adapter ----

		WeatherAdapter adapter = new WeatherAdapter(getActivity().getApplicationContext(),
				weatherForcastData.getWeatherList(),
				R.layout.weather_item,
				new String[] {WeatherForcast.MAX},
				new int[] { R.id.item_weather_sun },
				weatherForcastData.getAverage());

		listView.setAdapter(adapter);	

		// ---- Set header views ----

		// Set city
		TextView cityView = (TextView)fragmentView.findViewById(R.id.fragment_weather_city);
		cityView.setText(weatherForcastData.getCityName());		

		// Set start and end dates
		TextView dateFromView = (TextView)fragmentView.findViewById(R.id.fragment_weather_date_from);				
		dateFromView.setText(weatherForcastData.getStartDate());
		TextView dateToView = (TextView)fragmentView.findViewById(R.id.fragment_weather_date_to);				
		dateToView.setText(weatherForcastData.getEndDate());
	}	

	private class JsonAsyncLoad extends AsyncTask<URL, Void, WeatherForcastData>
	{		
		private Button button;
		private ProgressBar progressBar;		

		@Override
		protected void onPreExecute()
		{		
			super.onPreExecute();			

			if( fragmentView != null )
			{
				// Replace button with a progress bar.
				button = (Button)fragmentView.findViewById(R.id.fragment_weather_search_button);			
				final LayoutParams params = button.getLayoutParams();	
				final ViewGroup viewGroup = (ViewGroup)button.getParent();
				viewGroup.removeView(button);
				progressBar = new ProgressBar(getActivity());		
				viewGroup.addView(progressBar,1,params);
			}
		}	

		protected WeatherForcastData doInBackground(URL... urls)
		{
			GsonHelper gsonHelper = new GsonHelper();			
			return gsonHelper.loadWeatherData(getActivity().getBaseContext(), urls[0]);			
		}			

		@Override
		protected void onPostExecute(WeatherForcastData result)
		{	
			if( getActivity() == null ) return;

			if( result != null && fragmentView != null )
			{				
				weatherForcastData = result;
				setListAdapterAndHeader();
				button.setText(getResources().getString(R.string.button_search));				

				// Set trim search text.
				final EditText searchCity = (EditText)fragmentView.findViewById(R.id.fragment_weather_search_city);
				final String city = searchCity.getText().toString().trim();
				searchCity.setText(city); // Set the trim operation.
				searchCity.setSelection(city.length()); // Move the cursor to the end.

				// Hide the keyboard.
				final InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(searchCity.getWindowToken(), 0);

				fragmentView.invalidate();
			}
			else
				button.setText(R.string.button_search_problem);

			if( progressBar != null && button != null )
			{
				// Replace back the progress bar with the button.
				final ViewGroup viewGroup = (ViewGroup)progressBar.getParent();
				viewGroup.removeView(progressBar);				
				viewGroup.addView(button, 1);				
			}
		}

		@Override
		protected void onCancelled()
		{				
			super.onCancelled();			
		}
	}

	@Override
	public void onClick(View v)
	{
		// Get the search text.
		final EditText searchCity = (EditText)fragmentView.findViewById(R.id.fragment_weather_search_city);
		final String city = searchCity.getText().toString().trim();				

		if( !city.equalsIgnoreCase("update") )
		{
			final URL url = setCityNameToUrl(city);	
			new JsonAsyncLoad().execute(url);
		}
		else
			updateListItem(); // Demo like updating the adapter data from the server.
	}

	/**
	 * Only for tests !!! Demo
	 */
	private void updateListItem()
	{
		// Change data.
		List<HashMap<String, Object>> list = (List<HashMap<String, Object>>)weatherForcastData.getWeatherList();
		list.get(2).put(WeatherForcast.MAX,19.4F); // for debug only.

		// Notify adapter.
		WeatherAdapter adapter = (WeatherAdapter)listView.getAdapter();
		adapter.notifyDataSetChanged();		
	}

	private URL setCityNameToUrl(String city)
	{
		URL weatherUrl = null;

		try
		{
			final String urlString = MainActivity.URL_WEATHER_FORCAST.replace("MyCity", city.trim());
			weatherUrl = new URL(urlString);		
		}
		catch(MalformedURLException e)
		{			
			e.printStackTrace();
			Toast toast = Toast.makeText(getActivity().getApplicationContext(), "City text is invalid.",Toast.LENGTH_LONG);
			toast.show();
		}	

		return weatherUrl;
	}

	private class IndexAsyncLoad extends AsyncTask<CountDownLatch, Void, SortedSet<String>>
	{		
		private final static String INDEX_FILE = "AutocompleteIndex.txt";	

		@Override
		protected SortedSet<String> doInBackground(CountDownLatch... params)
		{		
			final SortedSet<String> citiesSet = loadIndexFromFile(getActivity());

			try {
				params[0].await(); // Wait for the fragment to inflate its UI.
			} catch (InterruptedException e) {		
				e.printStackTrace();
			}

			return citiesSet;	
		}

		@Override
		protected void onPostExecute(SortedSet<String> citiesSet)
		{			
			Log.d(TAG,"onPostExecute(..)");

			if( fragmentView != null && citiesSet != null )
			{
				final AutoCompleteTextView autoComplete = (AutoCompleteTextView)fragmentView.findViewById(R.id.fragment_weather_search_city);				
				final AutocompleteAdapter adapter = new AutocompleteAdapter(fragmentView.getContext(), android.R.layout.simple_list_item_1, citiesSet);					
				autoComplete.setAdapter(adapter);
			}			
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
}