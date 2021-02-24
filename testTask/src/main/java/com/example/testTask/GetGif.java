package com.example.testTask;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "GifClient", url = "${giphy_url}")
public interface GetGif {
    @RequestMapping(method = RequestMethod.GET,value = "/v1/gifs/search")
    public JsonNode retrieveGif(@RequestParam("api_key")String apy_key,
                                @RequestParam("q") String q,
                                @RequestParam("limit")String limit,
                                @RequestParam("offset")String offset,
                                @RequestParam("rating")String rating,
                                @RequestParam("lang")String lang);
}
