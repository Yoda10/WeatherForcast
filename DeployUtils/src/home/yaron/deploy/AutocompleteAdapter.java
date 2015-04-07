package home.yaron.deploy;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

public class AutocompleteAdapter extends ArrayAdapter<String>
{
	private static final String TAG = AutocompleteAdapter.class.getSimpleName();

	private CitiesFilter citiesFilter = null;
	private final Object mLock = new Object(); // a copy form super	
	private SortedSet<String> mOriginalValues = null; 

	public AutocompleteAdapter(Context context, int textViewResourceId, SortedSet<String> data)
	{		
		super(context, textViewResourceId);
		Log.d(TAG, "AutocompleteAdapter(..)");
		mOriginalValues = data; // Cities list.
	}	

	@Override
	public Filter getFilter()
	{
		if(citiesFilter == null)		
			citiesFilter = new CitiesFilter();

		return citiesFilter;
	}	

	/**
	 * <p>An array filter constrains the content of the array adapter with
	 * a prefix. Each item that does not start with the supplied prefix
	 * is removed from the list.</p>
	 */
	private class CitiesFilter extends Filter
	{
		@Override
		protected FilterResults performFiltering(CharSequence prefix)
		{			
			FilterResults results = new FilterResults();			

			if(prefix == null || prefix.length() == 0) // No filter
			{ 
				final HashSet<String> unfilterSet;
				synchronized(mLock) {
					unfilterSet = new HashSet<String>(mOriginalValues.subSet("Aba","Abazzzzzzzz"));
				}
				results.values = unfilterSet;
				results.count = unfilterSet.size();
			}
			else // Filter
			{	
				// Convert every word first letter to upper case.
				final String prefixString = prefixToCapitalLetters(prefix.toString());

				final HashSet<String> valuesSet;
				synchronized (mLock) {
					valuesSet = new HashSet<String>(mOriginalValues.subSet(prefixString, prefixString+"zzzzzzzz"));
				}				

				results.values = valuesSet;
				results.count = valuesSet.size();
			}

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results)
		{
			AutocompleteAdapter.this.clear();				
			AutocompleteAdapter.this.addAll((Set<String>)results.values);					
		}
	}

	private String prefixToCapitalLetters(String prefix)
	{
		StringBuilder upperPrefix = new StringBuilder();
		final String[] wordList = prefix.trim().split(" ");
		for( String aWord : wordList)
		{	
			if( aWord.length() > 0 )
			{				
				final String firstLetter = aWord.substring(0,1).toUpperCase(Locale.US);
				upperPrefix.append(firstLetter + aWord.substring(1) + " ");
			}
		}

		return upperPrefix.toString().trim();
	}
}
