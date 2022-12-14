package co.com.sofkoin.alpha.application.usecases;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkoin.alpha.application.commons.UseCase;
import co.com.sofkoin.alpha.application.gateways.DomainEventBus;
import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.common.values.CryptoSymbol;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import co.com.sofkoin.alpha.domain.market.commands.PublishP2POffer;
import co.com.sofkoin.alpha.domain.market.entities.root.Market;
import co.com.sofkoin.alpha.domain.market.values.OfferCryptoAmount;
import co.com.sofkoin.alpha.domain.market.values.OfferCryptoPrice;
import co.com.sofkoin.alpha.domain.market.values.identities.MarketID;
import co.com.sofkoin.alpha.domain.market.values.identities.OfferId;
import co.com.sofkoin.alpha.domain.user.entities.root.User;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@AllArgsConstructor
@Component
public class PublishP2POfferUseCase implements UseCase<PublishP2POffer> {

  DomainEventBus domainEventBus;
  DomainEventRepository domainEventRepository;

  @Override
  public Flux<DomainEvent> apply(Mono<PublishP2POffer> publishP2POfferCommand) {
    return publishP2POfferCommand.flatMapMany(command ->
            domainEventRepository.findByAggregateRootId(command.getMarketId())
                    .switchIfEmpty(Mono.error(new IllegalArgumentException("The market with the given id does not exist.")))
                    .collectList()
                    .flatMap(events ->
                            domainEventRepository
                            .findByAggregateRootId(command.getPublisherId())
                            .switchIfEmpty(Mono.error(new IllegalArgumentException("The user with the given id does not exist.")))
                            .collectList()
                            .doOnNext(userEvents -> {
                              User user = User.from(new UserID(command.getPublisherId()), userEvents);

                              Double userCryptoAmount = user.findCryptoAmountBySymbol(command.getCryptoSymbol().trim());

                              if (command.getOfferCryptoAmount() > userCryptoAmount) {
                                throw new IllegalArgumentException("the user doesn't count with enough crypto funds to publish the given offer.");
                              }
                            })
                            .thenReturn(events))
                    .flatMapIterable(events -> {
                      Market market = Market.from(new MarketID(command.getMarketId()), events);
                      market.publishP2POffer(
                              new OfferId(),
                              new MarketID(command.getMarketId()),
                              new UserID(command.getPublisherId()),
                              new CryptoSymbol(command.getCryptoSymbol()),
                              new OfferCryptoAmount(command.getOfferCryptoAmount()),
                              new OfferCryptoPrice(command.getOfferCryptoPrice()),
                              new UserID(command.getTargetAudienceId()));

                      return market.getUncommittedChanges();
                    })
                    .map(event -> {
                      log.info("PublishP2POfferUseCase working");
                      domainEventBus.publishEvent(event);
                      return event;
                    })
                    .flatMap(event -> domainEventRepository.saveDomainEvent(event)));
  }

}
