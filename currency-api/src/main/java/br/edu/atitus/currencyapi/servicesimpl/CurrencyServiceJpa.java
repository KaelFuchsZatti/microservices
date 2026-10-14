package br.edu.atitus.currencyapi.servicesimpl;

import br.edu.atitus.currencyapi.clients.BcbCurrencyClientService;
import br.edu.atitus.currencyapi.clients.BcbQuoteUnavailableException;
import br.edu.atitus.currencyapi.dtos.CurrencyResponse;
import br.edu.atitus.currencyapi.repositories.CurrencyRepository;
import br.edu.atitus.currencyapi.services.CurrencyService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class CurrencyServiceJpa implements CurrencyService {

    private final CurrencyRepository repository;
    private final BcbCurrencyClientService bcbCurrencyClientService;

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${bcb.quote-date:05-30-2025}")
    private String quoteDate;

    public CurrencyServiceJpa(
            CurrencyRepository repository,
            BcbCurrencyClientService bcbCurrencyClientService) {
        this.repository = repository;
        this.bcbCurrencyClientService = bcbCurrencyClientService;
    }

    @Override
    public CurrencyResponse findBySourceCurrencyAndTargetCurrency(String sourceCurrency, String targetCurrency) throws Exception {
        String source = sourceCurrency.toUpperCase(Locale.ROOT);
        String target = targetCurrency.toUpperCase(Locale.ROOT);
        String environment = "Currency API running in port " + serverPort;

        if (source.equals(target)) {
            return new CurrencyResponse(source, target, 1.0, environment + " - Identical currencies");
        }

        try {
            BigDecimal sourceRate = getBrlRate(source);
            BigDecimal targetRate = getBrlRate(target);
            double conversionRate = sourceRate.divide(targetRate, MathContext.DECIMAL64).doubleValue();
            return new CurrencyResponse(
                    source,
                    target,
                    conversionRate,
                    environment + " - Banco Central do Brasil"
            );
        } catch (RestClientException | CallNotPermittedException | BcbQuoteUnavailableException exception) {
            var currency = repository.findBySourceCurrencyAndTargetCurrency(source, target)
                    .orElseThrow(() -> new EntityNotFoundException("Currency não encontrado"));
            return new CurrencyResponse(
                    currency.getSourceCurrency(),
                    currency.getTargetCurrency(),
                    currency.getConversionRate(),
                    environment + " - Fallback local: " + exception.getMessage()
            );
        }
    }

    private BigDecimal getBrlRate(String currency) {
        if ("BRL".equals(currency)) {
            return BigDecimal.ONE;
        }
        return bcbCurrencyClientService.getSellRate(currency, quoteDate)
                .orElseThrow(() -> new BcbQuoteUnavailableException(
                        "Sem cotação do BCB para " + currency + " em " + quoteDate
                ));
    }
}
