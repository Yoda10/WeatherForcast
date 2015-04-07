package home.yaron.deploy;
import java.util.ArrayList;

public class CountryList
{	
	ArrayList<Country> allCountries = new ArrayList<Country>();	

	void addCountryName(String countryName)
	{
		allCountries.add(new Country(countryName));
	}

	void addCityName(String cityName)
	{
		allCountries.get(allCountries.size()-1).addCity(cityName);
	}

	class Country
	{		
		String country;
		ArrayList<String> cities = new ArrayList<String>();

		Country(String countryName)
		{
			country = countryName;
		}

		private void addCity(String cityName)
		{
			cities.add(cityName);
		}		
	}	
}
