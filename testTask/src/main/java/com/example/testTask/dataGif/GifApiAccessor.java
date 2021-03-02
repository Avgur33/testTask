package com.example.testTask.dataGif;

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

@Component
@PropertySource("classpath:application.properties")
public class GifApiAccessor {

    @Value("${giphy.app_id}")
    private String app_id;
    @Value("${giphy.q_more:rich}")
    private String q_more;
    @Value("${giphy.q_less:broke}")
    private String q_less;
    @Value("${giphy.limit:1}")
    private String limit;
    @Value("${giphy.offset.min:0}")
    private String minOffset;
    @Value("${giphy.offset.max:4999}")
    private String maxOffset;
    @Value("${giphy.rating:g}")
    private String rating;
    @Value("${giphy.lang:en}")
    private String lang;
    @Value("${gif.source}")
    private String gifSource;

    @Autowired
    GetGif getGifApi;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getGifBroke(){
        return getGif(q_less);
    }

    public String getGifRich(){
        return getGif(q_more);
    }

    private String getGif(String q) {
        GifBean gifBean;
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode gifJson = getGifApi.retrieveGif(app_id, q, limit, getRandomNumber(), rating, lang);
            gifBean = mapper.treeToValue(gifJson, GifBean.class);

            if (gifBean.getPagination().getCount()==0){
                gifJson = getGifApi.retrieveGif(app_id, q, limit, getRandomNumber(gifBean.getPagination().getTotal_count()-1), rating, lang);
                gifBean = mapper.treeToValue(gifJson, GifBean.class);
            }

            if (gifBean.getMeta().getStatus() != 200) {
                logger.error(gifBean.getMeta().getMsg());
                return "Can`t get gif";
            }
        } catch (FeignException | JsonProcessingException e) {
            logger.error("Not authorized to get gif", e);
            return "Not authorized to get gif";
        }

        if ( gifBean.getData().length == 0) {
            logger.error("data invalid in response from giphy");
            return "bad connection try again";
        }else{
            return String.format(gifSource,gifBean.getData()[0].getId());
        }
    }
    private String getRandomNumber(Integer max) {
        int min = Integer.parseInt(minOffset);
        int res = (int)((Math.random() * (max - min)) + min);
        return Integer.toString(res);
    }

    private String getRandomNumber() {
        int min = Integer.parseInt(minOffset);
        int max = Integer.parseInt(maxOffset);
        int res = (int)((Math.random() * (max - min)) + min);
        return Integer.toString(res);
    }
}
