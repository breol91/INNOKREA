package pl.kwasek.innokrea.transport.connections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.cfg.NotYetImplementedException;
import org.jgrapht.DirectedGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

class Graph {

	DirectedGraph<String, DefaultEdge> graph;

	Graph() {
		this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
	}

	public void add(Connection connection) {
		String first = null;
		String second = null;
		for (String station : connection.getStations()) {
			first = second;
			second = station;
			if (first != null && second != null) {
				graph.addVertex(first);
				graph.addVertex(second);
				graph.addEdge(first, second);
			}
		}
	}

	public void remove(Connection connection) {
		throw new NotYetImplementedException();
	}

	public Collection<Route> getRoutes(String startStation, String destinationStation) {
		if(startStation.equals(destinationStation)) {
			List<Route> result = new ArrayList<>();
			result.add(new Route(startStation));
			return result;
		}
		
		if(!graph.containsVertex(startStation)) {
			return new ArrayList<>();
		}
		
		if(!graph.containsVertex(destinationStation)) {
			return new ArrayList<>();
		}
		
		AllDirectedPaths<String, DefaultEdge> query = new AllDirectedPaths<>(graph);
		List<GraphPath<String, DefaultEdge>> allPaths = query.getAllPaths(startStation, destinationStation, false, 10);
		List<Route> result = new ArrayList<>();
		for (GraphPath<String, DefaultEdge> path : allPaths) {
			result.add(new Route(path.getVertexList()));
		}
		return result;
	}

}
