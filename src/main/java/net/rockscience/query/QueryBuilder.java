package net.rockscience.query;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@ToString
@EqualsAndHashCode
public class QueryBuilder {
	@Getter
	private StringBuilder hql = new StringBuilder();
	private Map<String, Object> params = new HashMap<>();
	private int paramSerial = 0;
	private String paramKeyPrefix = "P";

	/**
	 * Package Protected getter for test
	 * @return
	 */
	Map<String, Object> getParams() {
		return params;
	}

	public String toHqlString() {
		return hql.toString();
	}
	
	/**
	 * Builder to add some arbitrary QL to the query string.  Insert a space between
	 * the existing and new as necessary 
	 * @param sql
	 * @return this object
	 */
	public QueryBuilder withQL(String ql) {
		if(!StringUtils.isEmpty(hql) && !StringUtils.endsWith(hql, " ") && !StringUtils.startsWith(ql, " ")) {
			hql.append(" ");
		}
		hql.append(ql);
		return this;
	}
	
	/**
	 * Override the default tokenized parameter prefix. This is useful if 
	 * multiple instances are to be combined without param keys colliding
	 * @param prefix
	 * @return
	 */
	public QueryBuilder withParamPrefix(String prefix) {
		this.paramKeyPrefix = prefix;
		return this;
	}
	
	/**
	 * Builder to add an ANDED condition to the query
	 * @param o
	 * @return this object
	 */
	public QueryBuilder withAndedCondition(Object o) {
		appendAndOrWhere();
		hql.append(o);
		return this;
	}

	/**
	 * Builder to add a Parameter Object to the map
	 * @param k
	 * @param v
	 * @return this object
	 */
	public QueryBuilder withNamedParam(String k, Object v) {
		params.put(k, v);
		return this;
	}
	
	
	/**
	 * Add a tokenized added condition. In the condition string, you can provide
	 * the variable name as this placeholder  ":$"
	 * For Example:  "ticket.ticketNumber = :$"
	 * @param condition
	 * @param paramValue
	 * @return
	 */
	public QueryBuilder withAndedConditionAndParamToken(String condition, Object paramValue) {
		if(paramValue != null) {
			String key = nextParamKey();
			condition = RegExUtils.replaceAll(condition, ":\\$", ":" + key);
			return withAndedCondition(condition)
					.withNamedParam(key, paramValue);
		}
		// null param value is no-op
		return this;
	}
	

	/**
	 * Append another instance into this one combining all 
	 * params in the map.
	 * Each instance using tokenized param names can have unique
	 * keys by specifying the paramKeyPrefix
	 * @param other
	 */
	public void append(QueryBuilder other) {
		this.withQL(other.getHql().toString());
		this.params.putAll(other.params);
	}		
	
	/**
	 * Return a new unique param name token
	 * @return
	 */
	private String nextParamKey() {
		return String.format("%s%d", this.paramKeyPrefix, this.paramSerial++);
	}
	
	/**
	 * Append a WHERE clause or an AND conjunction depending on if any params have been added yet
	 * @param sb
	 */
	private void appendAndOrWhere() {
		if (!params.isEmpty()) {
			hql.append(" and ");
		}
		else {
			hql.append(" where ");
		}
	}
	
	public void appendAndOrWith() {
		if (!params.isEmpty()) {
			hql.append(" and ");
		}
		else {
			hql.append(" with ");
		}
	}

	/**
	 * make a value suitable for use in a LIKE query. 
	 * Replace any * with %.
	 * Add an % to the end of the string if not already
	 * @param s
	 * @return
	 */
	public static String likeString(String s) {
		if(s == null) {
			return null;
		}
		s = RegExUtils.replaceAll(s, "\\*", "%");
		if(!StringUtils.endsWith(s, "%")) {
			s = s + "%";
		}
		return s;
	}

	public <T> TypedQuery<T> getQuery(EntityManager entityManager, Class<T> clazz) throws Exception {
		TypedQuery<T> q = entityManager.createQuery(this.toHqlString(), clazz);
		addParams(q);
		return q;
	}
	
	/**
	 * Given a JPA {@link Query} object, add the parameter key-values
	 * @param query
	 * @throws Exception
	 */
	public void addParams(Query query) throws Exception {
		try {
			for(String key : params.keySet()) {
				Object o = params.get(key);
				query.setParameter(key, o);
			}
		}
		catch(Exception e) {
			throw e;
		}
	}
}
