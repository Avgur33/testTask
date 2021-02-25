package com.example.testTask;

import com.example.testTask.dataGif.GifImageBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@RestController
@PropertySource("classpath:application.properties")
public class TestTaskController {
    @Autowired
    Environment env;

    @Autowired
    ExchangeRate exchangeRate;

    @Autowired
    private GetGif getgif;

    //создание логера, настройки в отдельном файле properties
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }


    public CurrencyGetObject getFromCurrencyRate(String path){
        CurrencyGetObject result = null;
        JsonNode response = null;
        ObjectMapper mapper = new ObjectMapper();
        //Ловим исключение от feign
        try {
            response = exchangeRate.retrieveValue(path ,env.getProperty("openexchangerates.app_id"));
        } catch (FeignException.Unauthorized e) {
            String z = e.getMessage();
            logger.info(z);
            //обработать
        }

        try {
            result = mapper.treeToValue(response,CurrencyGetObject.class);
        } catch (JsonProcessingException e) {
            //обработать
        }
        return result;
    }


    //id - код валюты курс которой мы должны узнать по отношению к рублю (базовой валюты)
    @GetMapping("/currency/{id}")
    public String CheckCurrency (@PathVariable String id) {
        CurrencyGetObject curCurrencyRate = getFromCurrencyRate("latest.json");;
        CurrencyGetObject oldCurrencyRate = getFromCurrencyRate("historical/" +
                                                                dayYesterday(curCurrencyRate.getTimestamp()) +
                                                                ".json");

        Double todayValueRUB = curCurrencyRate.getRates().get(env.getProperty("openexchangerates.rate", "RUB"));
        Double oldValueRUB = oldCurrencyRate.getRates().get(env.getProperty("openexchangerates.rate", "RUB"));

        Double todayValueId = curCurrencyRate.getRates().get(id);
        Double oldValueId = oldCurrencyRate.getRates().get(id);

        double todayValue;
        double oldValue;

        todayValue = todayValueId / todayValueRUB;
        oldValue = oldValueId / oldValueRUB;

        JsonNode gifJson = null;

        if (todayValue > oldValue) {
            gifJson = getgif.retrieveGif(env.getProperty("giphy.app_id"),
                    env.getProperty("giphy.q_more"),
                    env.getProperty("giphy.limit"),
                    env.getProperty("giphy.offset"),
                    env.getProperty("giphy.rating", "g"),
                    env.getProperty("giphy.lang", "en"));
        }else if (todayValue < oldValue){
            gifJson = getgif.retrieveGif(env.getProperty("giphy.app_id"),
                    env.getProperty("giphy.q_less"),
                    env.getProperty("giphy.limit"),
                    env.getProperty("giphy.offset"),
                    env.getProperty("giphy.rating", "g"),
                    env.getProperty("giphy.lang", "en"));
        }else{

        }

        GifImageBean gifBean = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            gifBean = mapper.treeToValue(gifJson, GifImageBean.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        StringBuilder result = new StringBuilder();
        //result.append(String.format("todayValue = %f<br> oldValue = %f<br> %s",(Double)todayValue,(Double)oldValue,dayYesterday(curCurrencyRate.getTimestamp())));
        String gifUrl = getGifUrl(gifBean);
        //result.append("<br> <a href=\"").append(gifUrl).append("\">").append(gifUrl).append("</a>");
        result.append("<img style=\"-webkit-user-select: none;margin: auto;background-color: hsl(0, 0%, 90%);transition: background-color 300ms;\" src=\"").append(gifUrl).append("\">");
        return result.toString();
    }

    private String getGifUrl(GifImageBean gifImageBean){
        if (gifImageBean == null) return "";
        String str_url = "";
        for (Object obj : gifImageBean.getData()) {
            if (((Map<String, Object>) obj).containsKey("images")) {
                str_url = (String) ((Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) obj).get("images")).get("original")).get("url");
            }
        }
        str_url = str_url.substring(str_url.lastIndexOf("media/")+6,str_url.lastIndexOf("/"));
        return "https://i.giphy.com/media/"+str_url+"/giphy.gif";
    }


    //получаем строку нужного формата содержащую вчерашнюю дату
    private String dayYesterday(BigDecimal curDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDay = new Date(curDate.longValue()*1000);
        Calendar cal = Calendar.getInstance();
        cal.setTime(todayDay);
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }
}
