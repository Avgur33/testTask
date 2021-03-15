package com.example.testTask.controller;

import com.example.testTask.dataCurrency.CurrencyApiAccessor;
import com.example.testTask.dataGif.GifApiAccessor;
import com.example.testTask.exception.ExternServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class TestTaskControllerTest {

    @MockBean
    private CurrencyApiAccessor currencyApi;

    @MockBean
    private GifApiAccessor gifApi;

    @Autowired
    private TestTaskController controller;

    @Test
    void checkCurrencyBroke() throws Exception {
        Mockito.doReturn(100d)
                .when(currencyApi)
                .getCurrentRate(ArgumentMatchers.anyString());

        Mockito.doReturn(200d)
                .when(currencyApi)
                .getYesterdayRate(ArgumentMatchers.anyString());

        String res = "<img style=\"-webkit-user-select: none;margin: auto;background-color: hsl(0, 0%, 90%);transition: background-color 300ms;\" src=\"123456789\">";
        Mockito.doReturn("123456789")
                .when(gifApi)
                .getGifBroke();

        String result = controller.checkCurrency("USD");

        Mockito.verify(gifApi, Mockito.times(1))
                .getGifBroke();

        Mockito.verify(gifApi, Mockito.times(0))
                .getGifRich();

        Assertions.assertTrue(result.contains(res));
    }

    @Test
    void checkCurrencyRich() throws Exception {
        Mockito.doReturn(200d)
                .when(currencyApi)
                .getCurrentRate(ArgumentMatchers.anyString());

        Mockito.doReturn(100d)
                .when(currencyApi)
                .getYesterdayRate(ArgumentMatchers.anyString());

        String res = "<img style=\"-webkit-user-select: none;margin: auto;background-color: hsl(0, 0%, 90%);transition: background-color 300ms;\" src=\"123456789\">";
        Mockito.doReturn("123456789")
                .when(gifApi)
                .getGifRich();

        String result = controller.checkCurrency("USD");

        Mockito.verify(gifApi, Mockito.times(0))
                .getGifBroke();

        Mockito.verify(gifApi, Mockito.times(1))
                .getGifRich();

        Assertions.assertTrue(result.contains(res));
    }

    @Test
    void checkCurrencyEquall() throws Exception {
        Mockito.doReturn(200d)
                .when(currencyApi)
                .getCurrentRate(ArgumentMatchers.anyString());

        Mockito.doReturn(200d)
                .when(currencyApi)
                .getYesterdayRate(ArgumentMatchers.anyString());

        String result = controller.checkCurrency("USD");

        Mockito.verify(gifApi, Mockito.times(0))
                .getGifBroke();

        Mockito.verify(gifApi, Mockito.times(0))
                .getGifRich();

        Assertions.assertTrue(result.contains("the exchange rate has not changed today"));
    }

    @Test
    void checkCurrencyIllegalAccess() throws Exception {
        when(currencyApi.getCurrentRate("lol")).thenThrow(IllegalAccessException.class);
        String result = controller.checkCurrency("lol");
        Assertions.assertTrue(result.contains("Illegal Currency Id"));
    }
    @Test
    void checkCurrencyExternExchange() throws Exception {
        when(currencyApi.getCurrentRate("USD")).thenThrow(ExternServiceException.class);
        String result = controller.checkCurrency("USD");
        Assertions.assertTrue(result.contains("Invalid service"));
    }

    @Test
    void checkCurrencyExternGifBroke() throws Exception {
        Mockito.doReturn(100d)
                .when(currencyApi)
                .getCurrentRate(ArgumentMatchers.anyString());

        Mockito.doReturn(200d)
                .when(currencyApi)
                .getYesterdayRate(ArgumentMatchers.anyString());

        when(gifApi.getGifBroke()).thenThrow(ExternServiceException.class);
        String result = controller.checkCurrency("USD");
        Assertions.assertTrue(result.contains("something wrong, please try again"));
    }

    @Test
    void checkCurrencyExternGifRich() throws Exception {
        Mockito.doReturn(200d)
                .when(currencyApi)
                .getCurrentRate(ArgumentMatchers.anyString());

        Mockito.doReturn(100d)
                .when(currencyApi)
                .getYesterdayRate(ArgumentMatchers.anyString());

        when(gifApi.getGifRich()).thenThrow(ExternServiceException.class);
        String result = controller.checkCurrency("USD");
        Assertions.assertTrue(result.contains("something wrong, please try again"));
    }
}
