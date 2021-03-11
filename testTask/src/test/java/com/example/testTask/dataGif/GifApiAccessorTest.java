package com.example.testTask.dataGif;

import com.example.testTask.exception.ExternServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class GifApiAccessorTest {

    @Autowired
    private GifApiAccessor gifApiAccessor;

    @MockBean
    GetGif getGifApi;

    private static JsonNode jsonResponse;

    @BeforeAll
    static void initJsonResponse() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("./src/test/java/" + GifApiAccessorTest.class.getPackage().getName().replace(".", "/") + "/gifRichResponse.json");
        jsonResponse = mapper.readTree(file);
    }

    @Test
    void getGifBroke() throws Exception {
        Mockito.doReturn(jsonResponse )
                .when(getGifApi)
                .retrieveGif(
                             ArgumentMatchers.eq("M23T9eKcSTBVv8GpFDzWYTEkb7RScupB")
                            ,ArgumentMatchers.eq("broke")
                            ,ArgumentMatchers.eq("1")
                            ,ArgumentMatchers.anyString()
                            ,ArgumentMatchers.eq("g")
                            ,ArgumentMatchers.anyString()
                );

        String result = gifApiAccessor.getGifBroke();
        Assertions.assertTrue(result.contains("https://i.giphy.com"));
    }

    @Test
    void errorGetGif() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree("{\"nick\": \"countdown\"}");
        Mockito.doReturn(jsonResponse )
                .when(getGifApi)
                .retrieveGif(
                             ArgumentMatchers.anyString()
                            ,ArgumentMatchers.anyString()
                            ,ArgumentMatchers.anyString()
                            ,ArgumentMatchers.anyString()
                            ,ArgumentMatchers.anyString()
                            ,ArgumentMatchers.anyString()
                );
        Exception exception = assertThrows(ExternServiceException.class, () -> {
            String result = gifApiAccessor.getGifBroke();
        });
    }
    @Test
    void zeroPaginationCount() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("./src/test/java/" + GifApiAccessorTest.class.getPackage().getName().replace(".", "/") + "/unvalidResponse.json");
        JsonNode jsonResponse = mapper.readTree(file);
        Mockito.doReturn(jsonResponse )
                .when(getGifApi)
                .retrieveGif(
                             ArgumentMatchers.anyString()
                            ,ArgumentMatchers.anyString()
                            ,ArgumentMatchers.anyString()
                            ,ArgumentMatchers.anyString()
                            ,ArgumentMatchers.anyString()
                            ,ArgumentMatchers.anyString()
                );

        String result = gifApiAccessor.getGifBroke();
        Assertions.assertTrue(result.contains("https://i.giphy.com"));

        Mockito.verify(getGifApi, Mockito.times(2))
                .retrieveGif(
                             ArgumentMatchers.anyString()
                            ,ArgumentMatchers.anyString()
                            ,ArgumentMatchers.anyString()
                            ,ArgumentMatchers.anyString()
                            ,ArgumentMatchers.anyString()
                            ,ArgumentMatchers.anyString()
                );
    }

    @Test
    void getGifRich() throws Exception{
        Mockito.doReturn(jsonResponse )
                .when(getGifApi)
                .retrieveGif(ArgumentMatchers.eq("M23T9eKcSTBVv8GpFDzWYTEkb7RScupB")
                            ,ArgumentMatchers.eq("rich")
                            ,ArgumentMatchers.eq("1")
                            ,ArgumentMatchers.anyString()
                            ,ArgumentMatchers.eq("g")
                            ,ArgumentMatchers.anyString());

        String result = gifApiAccessor.getGifRich();
        Assertions.assertTrue(result.contains("https://i.giphy.com"));
    }
}