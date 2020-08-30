package pl.kwasek.innokrea.transport.connections;

import java.text.ParseException;
import java.util.Calendar;

class RoutesQuery {

	private Calendar departureDate;
	private String startStation;
	private String destinationStation;
	
	RoutesQuery(Calendar departureDate, String startStation, String destinationStation) {
		this.departureDate = departureDate;
		this.startStation = startStation;
		this.destinationStation = destinationStation;
	}

	public RoutesQuery(RoutesQueryDTO routeQuery) throws ParseException {
		this.startStation = routeQuery.getStartStation();
		this.destinationStation = routeQuery.getDestinationStation();
		this.departureDate = DepartureDate.parse(routeQuery.getDepartureDate());
	}

	public Calendar getDepartureDate() {
		return departureDate;
	}

	public String getStartStation() {
		return startStation;
	}

	public String getDestinationStation() {
		return destinationStation;
	}

}
