package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.skillbox.currency.exchange.client.CBRFClient;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.dto.CurrencyReplyDto;
import ru.skillbox.currency.exchange.dto.CurrencyShortDto;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.mapper.CurrencyMapper;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyMapper mapper;
    private final CurrencyRepository repository;

    private final CBRFClient cbrfClient;

    public CurrencyDto getById(Long id) {
        log.info("CurrencyService method getById executed");
        Currency currency = repository.findById(id).orElseThrow(() -> new RuntimeException("Currency not found with id: " + id));
        return mapper.convertToDto(currency);
    }

    public CurrencyReplyDto getAll() {
        log.info("CurrencyService method getAll executed");
        List<CurrencyShortDto> list = repository.findAll()
                .stream()
                .map(mapper::convertToShortDto)
                .collect(Collectors.toList());
        return new CurrencyReplyDto(list);
    }

    public Double convertValue(Long value, Long numCode) {
        log.info("CurrencyService method convertValue executed");
        Currency currency = repository.findByIsoNumCode(numCode);
        return value * currency.getValue();
    }

    public CurrencyDto create(CurrencyDto dto) {
        log.info("CurrencyService method create executed");
        return mapper.convertToDto(repository.save(mapper.convertToEntity(dto)));
    }

    @Scheduled(cron = "@hourly")
//    @Scheduled(cron = "*/10 * * * * *")
    private void updateCurrenciesInDB() {
        log.info("CurrencyService method updateCurrencies executed");
        List<Currency> list = cbrfClient.getCurrencies().getCurrencies();
        list.forEach(this::updateCurrency);
    }


    private void updateCurrency(Currency currency) {
        log.info("CurrencyService method updateCurrency executed for Currency: {}", currency);
        Currency currencyForUpdate = repository.findByIsoLetterCode(currency.getIsoLetterCode());
        if (currencyForUpdate == null) {
            create(mapper.convertToDto(currency));
            return;
        }
        currencyForUpdate.setValue(currency.getValue());
        repository.save(currencyForUpdate);
    }

}
