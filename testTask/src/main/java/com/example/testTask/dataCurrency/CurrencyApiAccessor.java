package com.example.testTask.dataCurrency;

import com.example.testTask.exception.ExternServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

@Component
@PropertySource("classpath:application.properties")
public class CurrencyApiAccessor {

    @Value("${openexchangerates.app_id}")
    private String app_id;

    @Value("${currency.base}")
    private String base;

    @Value("${yesterday.dateformat}")
    private String dateFormatString;

    @Autowired
    ExchangeRate exchangeRate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private CurrencyGetObject getFromCurrencyRate(String path) throws ExternServiceException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.treeToValue(exchangeRate.retrieveValue(path, app_id), CurrencyGetObject.class);

        } catch (FeignException e) {
            logger.error("Not authorized to get currency rates in path {}", path, e);
            throw new ExternServiceException("Invalid URL or openexchangerate service fell down");
        } catch (JsonProcessingException e) {
            logger.error("Can`t parse Json");
            throw new ExternServiceException("Invalid response from openexchangerate service");
        }
    }

    public double getCurrentRate(String currencyId) throws IllegalAccessException, ExternServiceException {
        CurrencyGetObject currencyGetObject = getFromCurrencyRate("latest.json");
        return calcRate(currencyId, currencyGetObject);
    }

    public double getYesterdayRate(String currencyId) throws IllegalAccessException, ExternServiceException {
        CurrencyGetObject currencyGetObject = getFromCurrencyRate("historical/" + dayYesterday() + ".json");
        return calcRate(currencyId, currencyGetObject);
    }

    private double calcRate(String currencyId, CurrencyGetObject currencies) throws IllegalAccessException {
        Double baseValue;
        Double rateValue;
        if (currencies.getRates().containsKey(base.toUpperCase())) {
            baseValue = currencies.getRates().get(base.toUpperCase());
        } else {
            logger.error("(Base)Invalid currency id value: {}", base.toUpperCase());
            throw new IllegalAccessException(String.format("(Base)Invalid currency id value: %s", base));
        }
        if (currencies.getRates().containsKey(currencyId.toUpperCase())) {
            rateValue = currencies.getRates().get(currencyId.toUpperCase());
        } else {
            logger.error("(Rate)Invalid currency id value: {}", currencyId.toUpperCase());
            throw new IllegalAccessException(String.format("(Rate)Invalid currency id value: %s", currencyId));
        }
        return rateValue / baseValue;
    }

    //get valid string with yesterday date
    private String dayYesterday() {
        DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }
}
