package pl.kwasek.innokrea.transport.connections;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

class DepartureDate {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private DepartureDate() {
		// class cannot be create
	}
	
	static Calendar parse(String date) throws ParseException {
	    Calendar instance = Calendar.getInstance();
	    instance.setTime(sdf.parse(date));
	    return instance;
	}

	public static String parse(Calendar departureDate) {
		return sdf.format(departureDate.getTime());
	}

}
