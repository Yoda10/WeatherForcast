package home.yaron.deploy;


import home.yaron.deploy.R;

import java.util.SortedSet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class TestActivity extends Activity 
{	
	private SortedSet<String> citiesSet = null;	
	private CountryList countryList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		final AutoCompleteTextView autoComplete = (AutoCompleteTextView)findViewById(R.id.auto_complete_text);
		autoComplete.setThreshold(1);

		// Load button
		final Context context = this;
		Button fileButton = (Button)findViewById(R.id.button1);		
		fileButton.setOnClickListener(new OnClickListener() {			

			@Override
			public void onClick(View v) {
				countryList = Helper.parseJsonCountriesFile(context);
				citiesSet = (SortedSet<String>)Helper.createCitiesSet(countryList);
				Helper.saveCitiesSetToFile(citiesSet);
				final AutocompleteAdapter adapter = new AutocompleteAdapter(context, android.R.layout.simple_list_item_1, citiesSet);					
				autoComplete.setAdapter(adapter);
			}
		});
	}
}
