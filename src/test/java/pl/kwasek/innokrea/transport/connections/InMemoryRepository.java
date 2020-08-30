package pl.kwasek.innokrea.transport.connections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class InMemoryRepository implements ConnectionsRepository {

	private Map<Long, Connection> map = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	@Override
	public Connection save(Connection entity) {
		map.put(Long.valueOf(entity.hashCode()), entity);
		return entity;
	}

	@Override
	public <S extends Connection> Iterable<S> saveAll(Iterable<S> entities) {
		entities.forEach(this::save);
		return entities;
	}

	@Override
	public Optional<Connection> findById(Long id) {
		return Optional.ofNullable(map.get(id));
	}

	@Override
	public boolean existsById(Long id) {
		return findById(id).isPresent();
	}

	@Override
	public Iterable<Connection> findAll() {
		return map.values();
	}

	@Override
	public Iterable<Connection> findAllById(Iterable<Long> ids) {
		List<Connection> result = new ArrayList<>();
		ids.forEach(id -> findById(id).ifPresent(result::add));
		return result;
	}

	@Override
	public long count() {
		return map.size();
	}

	@Override
	public void deleteById(Long id) {
		map.remove(id);
	}

	@Override
	public void delete(Connection entity) {
		map.remove(Long.valueOf(entity.hashCode()));
	}

	@Override
	public void deleteAll(Iterable<? extends Connection> entities) {
		entities.forEach(this::delete);
	}

	@Override
	public void deleteAll() {
		map.clear();
	}

}
