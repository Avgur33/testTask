package com.example.testTask;

import com.example.testTask.controller.TestTaskController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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

	@Test
	public void contextLoads() throws Exception{
		assertThat(controller).isNotNull();
		this.mockMvc.perform(get("/currency/usd"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("https://i.giphy.com")));
	}
	@Test
	public void errorCurrencyId() throws Exception{
		assertThat(controller).isNotNull();
		this.mockMvc.perform(get("/currency/opl"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Illegal Currency Id")));
	}
}
