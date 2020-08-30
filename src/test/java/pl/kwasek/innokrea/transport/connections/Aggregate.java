package pl.kwasek.innokrea.transport.connections;

class Aggregate {

	static ConnectionsAggregate empty() {
		return new ConnectionsAggregate(new InMemoryRepository());
	}

	static ConnectionsAggregate get(Connection... connections) {
		ConnectionsAggregate aggregate = empty();
		for (Connection connection : connections) {
			aggregate.add(connection);
		}
		return aggregate;
	}

}
