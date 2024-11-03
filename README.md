# Various JPA Uils

## QueryBuilder 
Class to build JPA queries and keep parameters and HQL together. 

Typical Usage:

```
QueryBuilder qb = new QueryBuilder()
	.withQL("select sr from SolutionResultEntity as sr")
	.withQL("join fetch sr.solution as sol")
	.withQL("join fetch sol.ruleSet as rs")
	.withAndedConditionAndParamToken("rs.org=:$", oe);

	List<SolutionResultEntity> sres = qb.getQuery(entityManager, SolutionResultEntity.class).getResultList();

```

## TSIDUtil
Class with various null-safe helpers for translating a TSID between string and long identifiers.

## Transforms\<T\> 
Contract interface for a JPA entity.  Provides methods to materialize service layer models out of Entities and to orchestrate the building of Entities (and relationships) from Service layer models

Typical Usage:
```
public class EventEntity implements Transforms<Event> {
	@Column(name = "event_time_utc")
	private LocalDateTime eventTimeUTC;

	@Column(name = "log", length = 120)
	private String log;

	@Column(name = "event_type", columnDefinition = ColumnTypes.ENUM_256)
	private Integer eventType;

	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name="fk_event_user_id"))
	private UserEntity user;


	@Override
	public Event fromEntity() {
		Event e = new Event();
		e.setTsid(TSID.from(id));
		e.setDate(MultizoneDateTime.fromUtc(eventTimeUTC));
		e.setEventType(StableOrderEnum.fromCode(EventType.values(), eventType));
		e.setLog(log);
		e.setUser(user.fromEntity());
		return e;
	}

	@Override
	public void fromServiceModel(Event s, EntityManager entityManager) {
		log = StringUtils.abbreviate(s.getLog(), 120);
		eventType = StableOrderEnum.toCode(s.getEventType());
		eventTimeUTC = MultizoneDateTime.toUtcLocal(s.getDate());
		user = entityManager.find(UserEntity.class, TSIDUtil.toLongOrZero(s.getUser().getTsid()));
	}
}
```


### Install Locally
mvn clean install

