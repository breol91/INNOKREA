package pl.kwasek.innokrea.transport.connections;

import java.util.ArrayList;
import java.util.Calendar;

class Connections {

	static Connection getAny() {
		return new Connection(Date.CURRENT, new ArrayList<>()) ;
	}

	static Connection get(Calendar departureDate, String... stations) {
		Connection connection = new Connection(departureDate, stations);
		return connection;
	}

	/**
	 * NOTE CURRENT date will be use. 
	 * Create Connections between given station in current date 
	 * @param stations
	 * @return
	 */
	static Connection get(String... stations) {
		return get(Date.CURRENT, stations);
	}
}
