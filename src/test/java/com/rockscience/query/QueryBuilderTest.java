package com.rockscience.query;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.Query;

@ExtendWith(MockitoExtension.class)
public class QueryBuilderTest {


	private QueryBuilder underTest;
	
	@Mock Query q;

	@BeforeEach
	public void setUp() throws Exception {
		underTest = new QueryBuilder();
	}

	@Test
	public void test() throws Exception {
		underTest.withQL("select * from thing t")
		.withAndedCondition("t.foo=:foo")
		.withNamedParam("foo", "FOO")
		.withAndedCondition("t.bar=:bar")
		.withNamedParam("bar", "BAR");
		
		assertEquals("select * from thing t where t.foo=:foo and t.bar=:bar", underTest.getHql().toString());
		assertEquals("FOO", underTest.getParams().get("foo"));
		assertEquals("BAR", underTest.getParams().get("bar"));
		
		underTest.addParams(q);
		Mockito.verify(q).setParameter("foo", "FOO");
		Mockito.verify(q).setParameter("bar", "BAR");
	}
	
	@Test
	public void testAndedConditionWithTokenParams() {
		underTest.withQL("select t from TicketEntity t")
			.withAndedConditionAndParamToken("t.id = :$", 1);
		
		String hql = underTest.getHql().toString();
		assertEquals("select t from TicketEntity t where t.id = :P0", hql);
	}
	

}
