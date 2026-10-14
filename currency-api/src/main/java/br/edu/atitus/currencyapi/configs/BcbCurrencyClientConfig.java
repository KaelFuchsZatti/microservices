package br.edu.atitus.currencyapi.configs;

import br.edu.atitus.currencyapi.clients.BcbCurrencyClient;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class BcbCurrencyClientConfig {

    private static final String BCB_BASE_URL =
            "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata";

    @Bean
    BcbCurrencyClient bcbCurrencyClient() {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(2));
        requestFactory.setReadTimeout(Duration.ofSeconds(5));

        var restClient = RestClient.builder()
                .baseUrl(BCB_BASE_URL)
                .requestFactory(requestFactory)
                .build();
        var adapter = RestClientAdapter.create(restClient);
        var proxyFactory = HttpServiceProxyFactory.builderFor(adapter).build();

        return proxyFactory.createClient(BcbCurrencyClient.class);
    }
}
