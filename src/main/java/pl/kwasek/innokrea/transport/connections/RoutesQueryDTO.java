package pl.kwasek.innokrea.transport.connections;

public class RoutesQueryDTO {

	private String departureDate;
	private String startStation;
	private String destinationStation;

	public RoutesQueryDTO(String departureDate, String startStation, String destinationStation) {
		this.departureDate = departureDate;
		this.startStation = startStation;
		this.destinationStation = destinationStation;
	}

	public String getDepartureDate() {
		return this.departureDate;
	}

	public String getStartStation() {
		return this.startStation;
	}

	public String getDestinationStation() {
		return this.destinationStation;
	}
}
