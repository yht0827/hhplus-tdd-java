package io.hhplus.tdd;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import io.hhplus.tdd.point.PointController;
import io.hhplus.tdd.point.PointFacade;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;

@DisplayName("포인트 컨트롤러 테스트")
@WebMvcTest(value = PointController.class)
public class InMemoryPointControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	private PointFacade pointFacade;

	@DisplayName("유저 포인트 조회 API")
	@Test
	void getUserPointTest() throws Exception {

		given(pointFacade.getPointById(any(long.class)))
			.willReturn(new UserPoint(1L, 1000L, System.currentTimeMillis()));

		mockMvc.perform(
				get("/point/1"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.point").exists())
			.andExpect(jsonPath("$.updateMillis").exists());
	}

	@DisplayName("유저 포인트 히스토리 조회 API")
	@Test
	void getUserPointHistoryTest() throws Exception {
		given(pointFacade.getPointHistoryByUserId(any(long.class)))
			.willReturn(List.of(
				new PointHistory(1L, 1L, 1000, TransactionType.CHARGE, System.currentTimeMillis()),
				new PointHistory(2L, 1L, 100, TransactionType.USE, System.currentTimeMillis())
			));

		mockMvc.perform(
				get("/point/1/histories")
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$.*.id").exists())
			.andExpect(jsonPath("$.*.userId").exists())
			.andExpect(jsonPath("$.*.amount").exists())
			.andExpect(jsonPath("$.*.type").exists())
			.andExpect(jsonPath("$.*.updateMillis").exists());
	}

	@DisplayName("포인트 충전 API")
	@Test
	void chargePointTest() throws Exception {

		given(pointFacade.chargePoint(any(long.class), any(long.class)))
			.willReturn(new UserPoint(1L, 100L, System.currentTimeMillis()));

		mockMvc.perform(
				patch("/point/1/charge")
					.content("3000")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.point").exists())
			.andExpect(jsonPath("$.updateMillis").exists())
		;
	}

	@DisplayName("포인트 사용 API")
	@Test
	void usePointTest() throws Exception {
		given(pointFacade.usePoint(any(long.class), any(long.class)))
			.willReturn(new UserPoint(1L, 100L, System.currentTimeMillis()));

		mockMvc.perform(
				patch("/point/1/use")
					.content("1000")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.point").exists())
			.andExpect(jsonPath("$.updateMillis").exists())
		;
	}

}
