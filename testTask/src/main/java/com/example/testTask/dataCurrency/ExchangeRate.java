package com.example.testTask.dataCurrency;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="${ExchangeRateService}", url="${openexchangerates.url}")
public interface ExchangeRate {
    @RequestMapping(method = RequestMethod.GET, value = "/api/{currency_path}")

    public JsonNode retrieveValue(@PathVariable("currency_path") String currency_path
                                 ,@RequestParam(value = "app_id")String app_id );
}
