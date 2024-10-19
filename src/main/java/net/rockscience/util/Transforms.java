package net.rockscience.util;

import jakarta.persistence.EntityManager;

/**
 * Interface for an Entity which can transform to and fromn a service model
 */
public interface Transforms<T> {
	/**
	 * create a service mopdel of type T from the entity
	 * @return An instantiated and transformed service layer model of type T
	 */
	public T fromEntity();

	/**
	 * Transfer the state from a service layer model into the entity
	 * @param s The service layer model of type T
	 * @param entityManager The {@link EntityManager} for orchestrating any other needed queries
	 */
	public void fromServiceModel(T s, EntityManager entityManager);
}
