package co.com.sofkoin.alpha.infrastructure.adapters.mongo;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.infrastructure.commons.json.JSONMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.regex.Pattern;

@Repository
@AllArgsConstructor
public class MongodbDomainEventRepositoryAdapter implements DomainEventRepository {
    private static final String EVENTS_COLLECTION = "events";
    private final ReactiveMongoTemplate mongodbTemplate;
    private final JSONMapper jsonMapper;

    @Override
    public Flux<DomainEvent> findByAggregateRootId(String id) {
        Query query = new Query(
                Criteria.where("aggregateRootId").is(id)
        ).with(Sort.by(Sort.Direction.ASC, "timestamp"));

        return this.mongodbTemplate
                .find(query, DomainEventDocument.class, EVENTS_COLLECTION)
                .sort(Comparator.comparing(DomainEventDocument::getTimestamp))
                .map(this::mapDomainEventDocumentToDomainEvent);
    }

    @Override
    public Flux<DomainEvent> findUserDomainEventsByEmail(String email) {
        String regexp = String.format("\\\"email\\\"\\:\\\"%s\\\"", email);
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        Query query = new Query(
                Criteria.where("JSONEvent").regex(pattern)
        ).with(Sort.by(Sort.Direction.ASC, "timestamp"));;

        return this.mongodbTemplate.find(query, DomainEventDocument.class, EVENTS_COLLECTION)
                .map(this::mapDomainEventDocumentToDomainEvent);
    }

    @Override
    public Mono<DomainEvent> saveDomainEvent(DomainEvent event) {
        DomainEventDocument document = new DomainEventDocument(
                event.aggregateRootId(),
                this.jsonMapper.writeToJson(event),
                event.getClass().getName()
        );
        return this.mongodbTemplate.save(document, EVENTS_COLLECTION)
                .map(this::mapDomainEventDocumentToDomainEvent);
    }

    private DomainEvent mapDomainEventDocumentToDomainEvent(DomainEventDocument document) {
        try {
            return (DomainEvent) this.jsonMapper.readFromJson(
                    document.getJSONEvent(),
                    Class.forName(document.getEventClasspath())
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
