package pl.kwasek.innokrea.transport.connections;

import java.util.Calendar;

class Date {

	static Calendar YESTERDAY = getYesterdayDate();
	static Calendar CURRENT = getCurrentDate();
	static Calendar TOMORROW = getTommorowDate();

	private static Calendar getYesterdayDate() {
		Calendar date = Calendar.getInstance();
		date.set(2020, 8, 14);
		return date;
	}

	private static Calendar getCurrentDate() {
		Calendar date = Calendar.getInstance();
		date.set(2020, 8, 15);
		return date;
	}

	private static Calendar getTommorowDate() {
		Calendar date = Calendar.getInstance();
		date.set(2020, 8, 16);
		return date;
	}

}
