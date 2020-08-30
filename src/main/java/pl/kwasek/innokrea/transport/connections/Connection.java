package pl.kwasek.innokrea.transport.connections;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
class Connection {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Basic
	@Temporal(TemporalType.DATE)
	private Calendar departureDate;
	
	@ElementCollection
	private List<String> stations;
	
	Connection(){
	}
	
	Connection(Calendar departureDate, List<String> stations) {
		this.departureDate = departureDate;
		this.stations = stations;
	}

	Connection(Calendar departureDate, String[] stations) {
		this.departureDate = departureDate;
		setStations(stations);
	}

	public Connection(ConnectionDTO dto) throws ParseException {
		this.departureDate = DepartureDate.parse(dto.getDate());
		this.stations = dto.getStations();
	}

	void setStations(String[] stations) {
		this.stations = new ArrayList<>();
		for (int i = 0; i < stations.length; i++) {
			this.stations.add(stations[i]);
		}
	}

	Calendar getDepartureDate() {
		return departureDate;
	}

	List<String> getStations() {
		return stations;
	}

	@Override
	public String toString() {
		return "Connection [departureDate=" + departureDate + ", stations=" + stations + "]";
	}
		
}
