package com.example.punkapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=PunkapiApplication.class)
@WebMvcTest(MainController.class)
class PunkapiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BeerRepository beerRepository;

	@MockBean
	private UserBeerRepository userBeerRepository;

	@BeforeEach
	void init() {
		List<Beer> beers = new ArrayList<>();
		beers.add(getBeer1());
		beers.add(getBeer2());

		Page<Beer> page = new PageImpl<>(beers);
		when(beerRepository.findAll(any(Pageable.class))).thenReturn(page);
		when(beerRepository.findByNameOrDescription(any(String.class), any(Pageable.class))).thenReturn(page);
	}
	
	@Test
	void testLoadBeers() throws Exception {
		this.mockMvc.perform(get("/beers")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0].id", is(getBeer1().getId())))
				.andExpect(jsonPath("$.content[0].name", is(getBeer1().getName())))
				.andExpect(jsonPath("$.content[1].id", is(getBeer2().getId())))
				.andExpect(jsonPath("$.content[1].name", is(getBeer2().getName())));
	}

	private Beer getBeer1(){
		Beer ret = new Beer();
		ret.setId(1);
		ret.setName("Jinx Pale Ale");
		ret.setDescription("A new 4.7% ABV American pale ale will feature evolving editions as part of our new subscription model.");
		ret.setAbv(4.7);
		return ret;
	}

	private Beer getBeer2(){
		Beer ret = new Beer();
		ret.setId(2);
		ret.setName("Jet Trash");
		ret.setDescription("Jet Trash is one of the core Fanzine beer");
		ret.setAbv(6.9);
		return ret;
	}


}
