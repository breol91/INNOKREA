package pl.kwasek.innokrea.transport.connections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.kwasek.innokrea.transport.connections.Date.CURRENT;
import static pl.kwasek.innokrea.transport.connections.Date.TOMORROW;
import static pl.kwasek.innokrea.transport.connections.Stations.GDANSK;
import static pl.kwasek.innokrea.transport.connections.Stations.GDYNIA;
import static pl.kwasek.innokrea.transport.connections.Stations.RUMIA;
import static pl.kwasek.innokrea.transport.connections.Stations.SOPOT;
import static pl.kwasek.innokrea.transport.connections.Stations.WEJHEROWO;

import java.util.Collection;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

class ConnectionsAggregateTest {

	@Test
	void testAddConnectionToEmptyList() {
		// given
		ConnectionsAggregate aggregate = Aggregate.empty();
		// when
		aggregate.add(Connections.getAny());
		// then
		assertEquals(1, aggregate.count());
	}

//	@Test
//	void testRemoveByConnection() {
//		// given
//		ConnectionsAggregate aggregate = Aggregate.empty();
//		Connection connection = Connections.getAny();
//		aggregate.add(connection);
//		// when
//		aggregate.remove(connection);
//		// then
//		assertEquals(0, aggregate.count());
//	}
//	
//	@Test
//	void testRemoveById() {
//		// given
//		ConnectionsAggregate aggregate = Aggregate.empty();
//		Connection connection = Connections.getAny();
//		aggregate.add(connection);
//		// when
//		aggregate.remove((long)connection.hashCode());
//		// then
//		assertEquals(0, aggregate.count());
//	}

	@Test
	void test_return_none_routes_when_repository_is_empty() {
		// given
		ConnectionsAggregate query = Aggregate.empty();
		// when
		Collection<Route> route = query.getRoutes(CURRENT, GDYNIA, GDANSK);
		// then
		assertTrue(route.isEmpty());
	}

	@Test
	void test_none_route_from_one_connection_without_any_match() {
		// given
		ConnectionsAggregate query = Aggregate.get(Connections.get(GDYNIA, SOPOT));
		// when
		Collection<Route> routes = query.getRoutes(TOMORROW, RUMIA, WEJHEROWO);
		// then
		assertTrue(routes.isEmpty());
	}

	@Test
	void test_none_route_from_one_connection_with_match_date() {
		// given
		ConnectionsAggregate query = Aggregate.get(Connections.get(GDYNIA, SOPOT));
		// when
		Collection<Route> routes = query.getRoutes(CURRENT, RUMIA, WEJHEROWO);
		// then
		assertTrue(routes.isEmpty());
	}

	@Test
	void test_none_route_from_one_connection_with_match_date_start_station() {
		// given
		ConnectionsAggregate query = Aggregate.get(Connections.get(GDYNIA, SOPOT));
		// when
		Collection<Route> routes = query.getRoutes(CURRENT, GDYNIA, RUMIA);
		// then
		assertTrue(routes.isEmpty());
	}

	@Test
	void test_one_route_from_one_connection_with_match_date_start_and_destination_station() {
		// given
		ConnectionsAggregate query = Aggregate.get(Connections.get(GDYNIA, SOPOT));
		// when
		Collection<Route> routes = query.getRoutes(CURRENT, GDYNIA, SOPOT);
		// then
		assertEquals(1, routes.size());
		Route route = routes.iterator().next();
		assertEquals(2, route.getStations().size());
		assertEqualsStations(route, GDYNIA, SOPOT);
	}

	@Test
	void test_one_route_from_two_connection_one_fully_match_second_without_match() {
		// given
		ConnectionsAggregate query = Aggregate.get(Connections.get(GDYNIA, SOPOT), Connections.get(RUMIA, WEJHEROWO));
		// when
		Collection<Route> routes = query.getRoutes(CURRENT, GDYNIA, SOPOT);
		// then
		assertEquals(1, routes.size());
		Route route = routes.iterator().next();
		assertEquals(2, route.getStations().size());
		assertEqualsStations(route, GDYNIA, SOPOT);
	}

	@Test
	void test_two_routes_from_two_connection_fully_match() {
		// given
		ConnectionsAggregate query = Aggregate.get(Connections.get(WEJHEROWO, GDANSK),
				Connections.get(WEJHEROWO, RUMIA, GDYNIA, SOPOT, GDANSK));
		// when
		Collection<Route> routes = query.getRoutes(CURRENT, WEJHEROWO, GDANSK);
		// then
		assertEquals(2, routes.size());
		Iterator<Route> iterator = routes.iterator();
		Route shorter = iterator.next();
		Route longer = iterator.next();
		if (shorter.getStationsCount() > longer.getStationsCount()) {
			Route tmp = shorter;
			shorter = longer;
			longer = tmp;
		}
		assertEquals(2, shorter.getStationsCount());
		assertEquals(5, longer.getStationsCount());
	}

	@Test
	void test_one_route_from_two_connections_with_change() {
		// given
		ConnectionsAggregate query = Aggregate.get(Connections.get(WEJHEROWO, RUMIA, GDYNIA),
				Connections.get(GDYNIA, SOPOT, GDANSK));
		// when
		Collection<Route> routes = query.getRoutes(CURRENT, WEJHEROWO, GDANSK);
		// then
		assertEquals(1, routes.size());
		Route route = routes.iterator().next();
		assertEquals(5, route.getStationsCount());
		assertEqualsStations(route, WEJHEROWO, RUMIA, GDYNIA, SOPOT, GDANSK);

	}

	@Test
	void test_two_routes_from_three_connections_differents_after_change() {
		// given
		Connection conn_1_0 = Connections.get(WEJHEROWO, RUMIA, GDYNIA);
		Connection conn_1_1 = Connections.get(GDYNIA, SOPOT, GDANSK);
		Connection conn_1_2 = Connections.get(GDYNIA, GDANSK);
		ConnectionsAggregate query = Aggregate.get(conn_1_0, conn_1_1, conn_1_2);
		// when
		Collection<Route> routes = query.getRoutes(CURRENT, WEJHEROWO, GDANSK);
		// then
		assertEquals(2, routes.size());
		Iterator<Route> iterator = routes.iterator();
		Route shorter = iterator.next();
		Route longer = iterator.next();
		if (shorter.getStationsCount() > longer.getStationsCount()) {
			Route tmp = shorter;
			shorter = longer;
			longer = tmp;
		}
		assertEqualsStations(shorter, WEJHEROWO, RUMIA, GDYNIA, GDANSK);
		assertEqualsStations(longer, WEJHEROWO, RUMIA, GDYNIA, SOPOT, GDANSK);
	}

	@Test
	void test_two_routes_from_three_connections_differents_before_change() {
		// given
		Connection conn_1_0 = Connections.get(WEJHEROWO, RUMIA, GDYNIA);
		Connection conn_1_1 = Connections.get(WEJHEROWO, GDYNIA);
		Connection conn_1_2 = Connections.get(GDYNIA, SOPOT, GDANSK);
		ConnectionsAggregate query = Aggregate.get(conn_1_0, conn_1_1, conn_1_2);
		// when
		Collection<Route> routes = query.getRoutes(CURRENT, WEJHEROWO, GDANSK);
		// then
		assertEquals(2, routes.size());
		Iterator<Route> iterator = routes.iterator();
		Route shorter = iterator.next();
		Route longer = iterator.next();
		if (shorter.getStationsCount() > longer.getStationsCount()) {
			Route tmp = shorter;
			shorter = longer;
			longer = tmp;
		}
		assertEqualsStations(shorter, WEJHEROWO, GDYNIA, SOPOT, GDANSK);
		assertEqualsStations(longer, WEJHEROWO, RUMIA, GDYNIA, SOPOT, GDANSK);
	}

	@Test
	void test_one_route_from_one_connection_with_start_and_destination_in_middle_of_route() {
		// given
		ConnectionsAggregate query = Aggregate.get(Connections.get(WEJHEROWO, RUMIA, GDYNIA, SOPOT, GDANSK));
		// when
		Collection<Route> routes = query.getRoutes(CURRENT, RUMIA, SOPOT);
		// then
		assertEquals(1, routes.size());
		assertEqualsStations(routes.iterator().next(), RUMIA, GDYNIA, SOPOT);
	}

	@Test
	void test_one_route_from_two_connection_with_change_start_in_the_middle_of_first_and_destination_in_the_middle_of_second() {
		// given
		Connection conn_1 = Connections.get(WEJHEROWO, RUMIA, GDYNIA);
		Connection conn_2 = Connections.get(GDYNIA, SOPOT, GDANSK);
		ConnectionsAggregate query = Aggregate.get(conn_1, conn_2);
		// when
		Collection<Route> routes = query.getRoutes(CURRENT, RUMIA, SOPOT);
		// then
		assertEquals(1, routes.size());
		assertEqualsStations(routes.iterator().next(), RUMIA, GDYNIA, SOPOT);
	}

	@Test
	void test_start_and_destination_stations_are_the_same_without_any_connection() {
		// given
		ConnectionsAggregate query = Aggregate.empty();
		// when
		Collection<Route> routes = query.getRoutes(CURRENT, GDANSK, GDANSK);
		// then
		assertEquals(0, routes.size());
	}

	@Test
	void test_start_and_destination_stations_are_the_same_with_one_connection_with_match_start_station() {
		// given
		ConnectionsAggregate query = Aggregate.get(Connections.get(GDANSK, SOPOT, GDYNIA));
		// when
		Collection<Route> routes = query.getRoutes(CURRENT, GDANSK, GDANSK);
		// then
		assertEquals(1, routes.size());
		assertEqualsStations(routes.iterator().next(), GDANSK);
	}

	@Test
	void test_start_and_destination_stations_are_the_same_with_one_connection_with_match_destination_station() {
		// given
		ConnectionsAggregate query = Aggregate.get(Connections.get(GDYNIA, SOPOT, GDANSK));
		// when
		Collection<Route> routes = query.getRoutes(CURRENT, GDANSK, GDANSK);
		// then
		assertEquals(1, routes.size());
		assertEqualsStations(routes.iterator().next(), GDANSK);
	}

	@Test
	void test_start_and_destination_stations_are_the_same_with_two_cyclic_connection() {
		// first_start_station = second_destination_station
		// first_destination_station = second_start_station
		// given
		Connection conn_1 = Connections.get(GDANSK, SOPOT, GDYNIA);
		Connection conn_2 = Connections.get(GDYNIA, SOPOT, GDANSK);
		ConnectionsAggregate query = Aggregate.get(conn_1, conn_2);
		// when
		Collection<Route> routes = query.getRoutes(CURRENT, GDANSK, GDANSK);
		// then
		assertEquals(1, routes.size());
		assertEqualsStations(routes.iterator().next(), GDANSK);

	}

	@Test
	void test_one_route_two_connections_two_stations_mache_to_change_first_longer() {
		// TODO consult with customer
		// given
		Connection conn_1 = Connections.get(WEJHEROWO, RUMIA, GDYNIA, SOPOT);
		Connection conn_2 = Connections.get(GDYNIA, SOPOT, GDANSK);
		ConnectionsAggregate query = Aggregate.get(conn_1, conn_2);
		// when
		Collection<Route> routes = query.getRoutes(CURRENT, WEJHEROWO, GDANSK);
		// then
		assertEquals(1, routes.size());
		assertEqualsStations(routes.iterator().next(), WEJHEROWO, RUMIA, GDYNIA, SOPOT, GDANSK);
	}

	@Test
	void test_one_route_two_connections_two_stations_mache_to_change_first_shorter() {
		// TODO consult with customer
		// given
		Connection conn_1 = Connections.get(WEJHEROWO, RUMIA, GDYNIA);
		Connection conn_2 = Connections.get(RUMIA, GDYNIA, SOPOT, GDANSK);
		ConnectionsAggregate query = Aggregate.get(conn_1, conn_2);
		// when
		Collection<Route> routes = query.getRoutes(CURRENT, WEJHEROWO, GDANSK);
		// then
		assertEquals(1, routes.size());
		assertEqualsStations(routes.iterator().next(), WEJHEROWO, RUMIA, GDYNIA, SOPOT, GDANSK);
	}

	private void assertEqualsStations(Route route, String... expectedStations) {
		assertEquals(expectedStations.length, route.getStationsCount());
		assertArrayEquals(expectedStations, route.getStations().toArray(new String[route.getStationsCount()]));
	}
}
