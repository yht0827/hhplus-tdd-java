package io.hhplus.tdd;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointController;
import io.hhplus.tdd.point.PointService;
import io.hhplus.tdd.point.UserPoint;

@DisplayName("포인트 컨트롤러 테스트")
@WebMvcTest(value = PointController.class)
public class PointControllerTest {

	@MockBean
	private PointService pointService;

	@Mock
	private UserPointTable userPointTable;

	@Mock
	private PointHistoryTable pointHistoryTable;

	@DisplayName("유저 포인트를 조회한다.")
	@Test
	void testGetUserPoint() {
		// given
		long userId = 1L;
		UserPoint MockUserPoint = new UserPoint(userId, 7000, System.currentTimeMillis());
		when(pointService.getUserPoint(userId)).thenReturn(MockUserPoint);

		// when


		// then
		assertThat(MockUserPoint).isEqualTo(pointService.getUserPoint(userId));
	}

}
