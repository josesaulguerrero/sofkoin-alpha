package co.com.sofkoin.alpha.infrastructure.adapters.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DomainEventDocument {
    @Id
    private String id;
    private String aggregateRootId;
    private String JSONEvent;
    private String eventClasspath;
    private LocalDateTime timestamp;

    public DomainEventDocument(String aggregateRootId, String JSONEvent, String eventClasspath) {
        this.aggregateRootId = aggregateRootId;
        this.JSONEvent = JSONEvent;
        this.eventClasspath = eventClasspath;
        this.timestamp = LocalDateTime.now();
    }
}
