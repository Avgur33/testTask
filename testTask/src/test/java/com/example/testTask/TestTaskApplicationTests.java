package com.example.testTask;

import com.example.testTask.controller.TestTaskController;
import com.example.testTask.dataCurrency.ExchangeRate;
import com.example.testTask.dataGif.GetGif;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class TestTaskApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestTaskController controller;

	@MockBean
	private ExchangeRate exchangeRate;

	@MockBean
	GetGif getGifApi;

	private static JsonNode lastResponseCurrencyRate;
	private static JsonNode historicalResponseCurrensyRate;
	private static JsonNode gifResponse;

	private final String currentRatePath ="latest.json";
	private final String yesterdayRatePath = "historical/%s.json";

	@BeforeAll
	static void initJsonResponse() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		ClassLoader classLoader = TestTaskApplicationTests.class.getClassLoader();

		File fileLast= new File(classLoader.getResource("dataCurrency/exchangeRateResponse.json").getFile());
		File fileHistorical = new File(classLoader.getResource("dataCurrency/historicalResponse.json").getFile());
		File fileGif = new File(classLoader.getResource("dataGif/gifRichResponse.json").getFile());
		gifResponse = mapper.readTree(fileGif);
		lastResponseCurrencyRate = mapper.readTree(fileLast);
		historicalResponseCurrensyRate = mapper.readTree(fileHistorical);
	}

	@Test
	public void testContext() throws Exception{
		assertThat(controller).isNotNull();

		Mockito.doReturn(lastResponseCurrencyRate)
				.when(exchangeRate)
				.retrieveValue(currentRatePath,ArgumentMatchers.anyString());

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);

		Mockito.doReturn(historicalResponseCurrensyRate )
				.when(exchangeRate)
				.retrieveValue(String.format(yesterdayRatePath,dateFormat.format(cal.getTime())),ArgumentMatchers.anyString());

		Mockito.doReturn(gifResponse )
				.when(getGifApi)
				.retrieveGif(
						 ArgumentMatchers.anyString()
						,ArgumentMatchers.anyString()
						,ArgumentMatchers.eq("1")
						,ArgumentMatchers.anyString()
						,ArgumentMatchers.eq("g")
						,ArgumentMatchers.anyString()
				);
		mockMvc.perform(get("/currency/usd"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("https://i.giphy.com")));
	}

	@Test
	public void testInvalidCurrencyId() throws Exception{
		assertThat(controller).isNotNull();

		Mockito.doReturn(lastResponseCurrencyRate)
				.when(exchangeRate)
				.retrieveValue(currentRatePath,ArgumentMatchers.anyString());

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);

		Mockito.doReturn(historicalResponseCurrensyRate )
				.when(exchangeRate)
				.retrieveValue(String.format(yesterdayRatePath,dateFormat.format(cal.getTime())),ArgumentMatchers.anyString());

		Mockito.doReturn(gifResponse)
				.when(getGifApi)
				.retrieveGif(
						 ArgumentMatchers.anyString()
						,ArgumentMatchers.anyString()
						,ArgumentMatchers.eq("1")
						,ArgumentMatchers.anyString()
						,ArgumentMatchers.eq("g")
						,ArgumentMatchers.anyString()
				);

		mockMvc.perform(get("/currency/lol"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Illegal Currency Id")));
	}
}
