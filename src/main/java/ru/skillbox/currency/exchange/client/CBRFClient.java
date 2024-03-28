package ru.skillbox.currency.exchange.client;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.skillbox.currency.exchange.entity.Currencies;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@Setter
public class CBRFClient {

    private HttpGet httpGet;
    @Value("${app.currencyAPI.secondUrl}")
    private String secondLink;
    private final HttpClient httpClient;

    public CBRFClient(@Value("${app.currencyAPI.url}") String link) {
        httpGet = new HttpGet(link);
        httpClient = HttpClientBuilder.create()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
    }

    public Currencies getCurrencies() {
        log.info("Performing client call to CBRF API to get values' prices");
        Currencies currencies = null;
        try {
            currencies = unmarshal();
        } catch (JAXBException | URISyntaxException | IOException e) {
            log.error("Error while getting call to CBRF API", e);
            e.printStackTrace();
        }
        return currencies;
    }

    public Currencies unmarshal() throws JAXBException, URISyntaxException, IOException {
        JAXBContext context = JAXBContext.newInstance(Currencies.class);
        try {
            String response = EntityUtils.toString(httpClient.execute(httpGet).getEntity());
            return (Currencies) context.createUnmarshaller()
                    .unmarshal(new StringReader
                            (response));
        } catch (IOException e) {
            HttpGet get = new HttpGet(secondLink);
            this.setHttpGet(get);
            String response = IOUtils.toString(
                    httpClient.execute(httpGet).getEntity().getContent(), StandardCharsets.UTF_8);
            return (Currencies) context.createUnmarshaller()
                    .unmarshal(new StringReader
                            (response));
        }
    }
}


