package home.yaron.deploy;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

public class AutocompleteAdapter extends ArrayAdapter<String>
{
	public AutocompleteAdapter(Context context, int textViewResourceId, SortedSet<String> data)
	{
		super(context, textViewResourceId);
		mOriginalValues = data;		
	}

	private YaronFilter yaronFilter = null;

	@Override
	public Filter getFilter() 
	{
		if (yaronFilter == null) {
			yaronFilter = new YaronFilter();
		}
		return yaronFilter;
	}

	private final Object mLock = new Object(); // a copy form super	
	private SortedSet<String> mOriginalValues = null; //new ArrayList<String>(Arrays.asList(vec));

	/**
	 * <p>An array filter constrains the content of the array adapter with
	 * a prefix. Each item that does not start with the supplied prefix
	 * is removed from the list.</p>
	 */

	private class YaronFilter extends Filter
	{
		@Override
		protected FilterResults performFiltering(CharSequence prefix)
		{			
			FilterResults results = new FilterResults();

			//			if(mOriginalValues == null) {
			//				synchronized (mLock) {
			//					mOriginalValues = new ArrayList<T>(mObjects);
			//				}
			//			}

			if(prefix == null || prefix.length() == 0) // No filter
			{ 
				HashSet<String> unfilterSet;
				synchronized (mLock) {
					unfilterSet = new HashSet<String>(mOriginalValues.subSet("Aba","Abasto"));
				}
				results.values = unfilterSet;
				results.count = unfilterSet.size();
			}
			else // Filter
			{
				//String prefixString = prefix.toString().toLowerCase();
				//String prefixString = prefix.toString();
				String prefixString = prefixToCapitalLetters(prefix.toString());

				HashSet<String> valuesSet;
				synchronized (mLock) {
					valuesSet = new HashSet<String>(mOriginalValues.subSet(prefixString, prefixString+"zzzzzzzz"));
				}

				//				final int count = valuesSet.size();
				//				final ArrayList<String> newValues = new ArrayList<String>();
				//
				//				for (int i = 0; i < count; i++) 
				//				{
				//					final String value = valuesSet.get(i);
				//					final String valueText = value.toString().toLowerCase();
				//
				//					// First match against the whole, non-splitted value
				//					if (valueText.startsWith(prefixString)) {
				//						newValues.add(value);
				//					} else {
				//						final String[] words = valueText.split(" ");
				//						final int wordCount = words.length;
				//
				//						// Start at index 0, in case valueText starts with space(s)
				//						for (int k = 0; k < wordCount; k++) {
				//							if (words[k].startsWith(prefixString)) {
				//								newValues.add(value);
				//								break;
				//							}
				//						}
				//					}
				//				}

				results.values = valuesSet;
				results.count = valuesSet.size();
			}

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results)
		{
			AutocompleteAdapter.this.clear();					
			AutocompleteAdapter.this.addAll((Set<String>) results.values);
			//AutocompleteAdapter.this.addAll(new String[]{"xx","yy"});
			//this.publishResults(constraint, results);			
		}
	}

	private String prefixToCapitalLetters(String prefix)
	{
		String upperPrefix = "";
		final String[] wordList = prefix.split(" ");
		for( String aWord : wordList)
		{			
			final String firstLetter = aWord.substring(0,1).toUpperCase(Locale.US);
			upperPrefix += firstLetter + aWord.substring(1) + " ";		
		}

		return upperPrefix.trim();
	}	
}
