package pl.kwasek.innokrea.transport.connections;

import java.util.ArrayList;
import java.util.List;

class Route {

	private List<String> stations;

	public Route(String station) {
		this.stations = new ArrayList<>();
		this.stations.add(station);
	}
	
	public Route(List<String> stations) {
		this.stations = stations;
	}

	public List<String> getStations() {
		return stations;
	}

	public int getStationsCount() {
		return getStations().size();
	}

	@Override
	public String toString() {
		return "Route [stations=" + stations + "]";
	}
	
}
