package pl.kwasek.innokrea.transport.connections;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/connections")
public class ConnectionsController {

	private ConnectionsAggregate aggregate;

	@Autowired
	public ConnectionsController(ConnectionsAggregate aggregate) {
		this.aggregate = aggregate;
	}

	@GetMapping
	public ResponseEntity<Collection<ConnectionDTO>> getConnections(){
		List<ConnectionDTO> result = new ArrayList<>();
		aggregate.getAll().forEach(e -> result.add(new ConnectionDTO(e)));
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/add")
	public ResponseEntity<ConnectionDTO> addConnection(@RequestBody ConnectionDTO connection) throws ParseException {
		Connection connection2 = new Connection(connection);
		aggregate.add(connection2);
		return ResponseEntity.ok(new ConnectionDTO(connection2));
	}

	@DeleteMapping("/{connectionId}")
	public ResponseEntity<Boolean> removeConnection(@PathVariable Long connectionId) {
		aggregate.remove(connectionId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

//	@GetMapping("/query")
//	public ResponseEntity<Collection<Route>> getRoute(@RequestBody RoutesQueryDTO routeQuery) {
//		try {
//			Collection<Route> routes = aggregate.getRoutes(new RoutesQuery(routeQuery));
//			return new ResponseEntity<>(routes, HttpStatus.OK);
//		} catch (ParseException e) {
//			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
//		}
//	}

	@GetMapping("/query")
	public ResponseEntity<Collection<Route>> getRoute(@RequestParam("date") String departureDate, @RequestParam("startStation") String startStation, @RequestParam("destinationStation") String destinationStation) {
		try {
			RoutesQueryDTO query = new RoutesQueryDTO(departureDate, startStation, destinationStation);
			Collection<Route> routes = aggregate.getRoutes(new RoutesQuery(query));
			return new ResponseEntity<>(routes, HttpStatus.OK);
		} catch (ParseException e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

}
