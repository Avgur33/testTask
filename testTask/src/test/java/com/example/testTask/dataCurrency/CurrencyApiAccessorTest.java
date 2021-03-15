package com.example.testTask.dataCurrency;

import com.example.testTask.exception.ExternServiceException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;


import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest

class CurrencyApiAccessorTest {

    @Autowired
    private CurrencyApiAccessor currencyApiAccessor;

    @MockBean
    private ExchangeRate exchangeRate;

    private final String currentRatePath ="latest.json";
    private final String appId = "87c59f543a1f43d7a462c1d33f02b7dd";

    private String yesterdayRatePath = "historical/%s.json";

    private static JsonNode jsonResponse;
    private static JsonNode historicalResponse;

    @BeforeAll
    static void initJsonResponse() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        ClassLoader classLoader = CurrencyApiAccessorTest.class.getClassLoader();

        File file = new File(classLoader.getResource("dataCurrency/exchangeRateResponse.json").getFile());
        File fileHistorical = new File(classLoader.getResource("dataCurrency/historicalResponse.json").getFile());
        jsonResponse = mapper.readTree(file);
        historicalResponse = mapper.readTree(fileHistorical);
    }

    @Test
    void getCurrentRate() throws Exception {
        Mockito.doReturn(jsonResponse )
                .when(exchangeRate)
                .retrieveValue(currentRatePath,appId);

        double currentRate = currencyApiAccessor.getCurrentRate("USD");
        Assertions.assertTrue(currentRate > 0 );
    }

    @Test
    void errorRateCurrencyIdValue() {
        Mockito.doReturn(jsonResponse )
                .when(exchangeRate)
                .retrieveValue(currentRatePath,appId);

        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            double currentRate = currencyApiAccessor.getCurrentRate("111");
        });
    }

    @Test
    void errorJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree("{\"nick\": \"countdown\"}");
        Mockito.doReturn(jsonResponse)
                .when(exchangeRate)
                .retrieveValue(currentRatePath,appId);
        Exception exception = assertThrows(ExternServiceException.class, () -> {
            double currentRate = currencyApiAccessor.getCurrentRate("USD");
        });
    }

    @Test
    void getYesterdayRate() throws Exception{
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Mockito.doReturn(historicalResponse )
                .when(exchangeRate)
                .retrieveValue(String.format(yesterdayRatePath,dateFormat.format(cal.getTime())),appId);

        double currentRate = currencyApiAccessor.getYesterdayRate("USD");
        Assertions.assertTrue(currentRate > 0 );
    }
}