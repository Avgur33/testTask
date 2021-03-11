package com.example.testTask.controller;

import com.example.testTask.dataCurrency.CurrencyApiAccessor;
import com.example.testTask.dataGif.GifApiAccessor;
import com.example.testTask.exception.ExternServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PropertySource("classpath:application.properties")
public class TestTaskController {
    @Autowired
    CurrencyApiAccessor currencyApi;

    @Autowired
    private GifApiAccessor gifApi;

    //id - currency (example : usd )
    @GetMapping("/currency/{id}")
    public String checkCurrency (@PathVariable String id) {

        String gifUrl;

        double todayCurrencyRate;
        double yesterdayCurrencyRate;

        try {
            todayCurrencyRate = currencyApi.getCurrentRate(id);
            yesterdayCurrencyRate = currencyApi.getYesterdayRate(id);
        } catch (IllegalAccessException | ExternServiceException e) {
            return e.getMessage();
        }

        try {
            if (todayCurrencyRate > yesterdayCurrencyRate) {
                gifUrl = gifApi.getGifRich();
            } else if (todayCurrencyRate < yesterdayCurrencyRate) {
                gifUrl = gifApi.getGifBroke();
            } else {
                return "the exchange rate has not changed today";
            }
        }catch (ExternServiceException e){
            return "something wrong, please try again";
        }
        return "<img style=\"-webkit-user-select: none;margin: auto;background-color: hsl(0, 0%, 90%);transition: background-color 300ms;\" src=\"" + gifUrl + "\">";
    }
}
