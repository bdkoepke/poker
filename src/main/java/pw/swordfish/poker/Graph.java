package pw.swordfish.poker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author brandon
 */
class Graph<K, V> {
	private final Vertex<K, V> root = new Vertex<>();

	private static <U, E> U forEachRemaining(Iterator<E> self, U identity, BiFunction<E, U, U> accumulator) {
		U u = identity;
		while (self.hasNext()) {
			E next = self.next();
			u = accumulator.apply(next, u);
		}
		return u;
	}

	public void addPath(Iterable<K> edges, V value) {
		Iterator<K> it = edges.iterator();
		final Vertex<K, V> leaf = findLeaf(it);
		forEachRemaining(it, leaf, (edge, oldVertex) -> {
			Vertex<K, V> newVertex = new Vertex<>();
			oldVertex.addVertex(edge, newVertex);
			return newVertex;
		}).value(value);
	}

	private Vertex<K, V> findLeaf(Iterator<K> edges) {
		Vertex<K, V> vertex = root;
		while (edges.hasNext()) {
			Optional<Vertex<K, V>> maybeVertex = vertex.tryGetVertex(edges.next());
			if (! maybeVertex.isPresent())
				return vertex;
			vertex = maybeVertex.get();
		}
		return vertex;
	}

	public Optional<V> get(Iterable<K> edges) {
		Vertex<K, V> vertex = root;
		for (K edge : edges) {
			Optional<Vertex<K, V>> maybeVertex = vertex.tryGetVertex(edge);
			if (! maybeVertex.isPresent())
				return Optional.empty();
			vertex = maybeVertex.get();
		}
		return vertex.value();
	}

	private static class Vertex<K, V> {
		private final Map<K, Vertex<K, V>> nodes = new HashMap<>();
		private Optional<V> maybeValue = Optional.empty();

		public Optional<V> value() { return maybeValue; }
		public void value(V value) { maybeValue = Optional.of(value); }

		public boolean isLeaf() {
			return nodes.isEmpty();
		}
		public void addVertex(K edge, Vertex<K, V> vertex) {
			this.nodes.put(edge, vertex);
		}
		public Optional<Vertex<K, V>> tryGetVertex(K edge) {
			return nodes.containsKey(edge) ?
					Optional.of(nodes.get(edge)) :
					Optional.empty();
		}
	}
}
