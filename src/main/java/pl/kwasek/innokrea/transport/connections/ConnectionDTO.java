package pl.kwasek.innokrea.transport.connections;

import java.util.List;

public class ConnectionDTO {

	private String date;
	private List<String> stations;
	
	public ConnectionDTO() {
	}
	
	public ConnectionDTO(Connection connection) {
		this.date = DepartureDate.parse(connection.getDepartureDate());
		this.stations = connection.getStations();
	}

	public List<String> getStations() {
		return this.stations;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setStations(List<String> stations) {
		this.stations = stations;
	}

}
