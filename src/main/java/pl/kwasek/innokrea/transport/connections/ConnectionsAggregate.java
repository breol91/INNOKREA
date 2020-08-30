package pl.kwasek.innokrea.transport.connections;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionsAggregate {

	private ConnectionsRepository repository;
	private Map<Calendar, Graph> graphs;

	@Autowired
	ConnectionsAggregate(ConnectionsRepository repository) {
		this.repository = repository;
		graphs = new HashMap<>();
		recoveryGraphs();
	}

	private void recoveryGraphs() {
		this.repository.findAll().forEach(this::addToGraphs);
	}

	public boolean add(Connection connection) {
		repository.save(connection); // is needed?
		addToGraphs(connection);
		return true;
	}

	void remove(Connection connection) {
		repository.delete(connection);
		removeFromGraphs(connection);
	}

	void remove(Long id) {
		Optional<Connection> findById = repository.findById(id);
		repository.deleteById(id);
		findById.ifPresent(this::removeFromGraphs);
	}

	long count() {
		return repository.count();
	}

	private void addToGraphs(Connection connection) {
		graphs.computeIfAbsent(connection.getDepartureDate(), e -> new Graph()).add(connection);
	}

	private void removeFromGraphs(Connection connection) {
		Graph graph = graphs.get(connection.getDepartureDate());
		if (graph != null) {
			graph.remove(connection);
		}
	}

	Collection<Route> getRoutes(RoutesQuery routesQuery) {
		return getRoutes(routesQuery.getDepartureDate(), routesQuery.getStartStation(),
				routesQuery.getDestinationStation());
	}

	Collection<Route> getRoutes(Calendar departureDate, String startStation, String destinationStation) {
		Graph graph = graphs.get(departureDate);
		if(graph != null) {
			return graph.getRoutes(startStation, destinationStation);
		}
		return new ArrayList<>();
	}

	public List<Connection> getAll() {
		List<Connection> result = new ArrayList<>();
		repository.findAll().forEach(result::add);
		return result;
	}

}
