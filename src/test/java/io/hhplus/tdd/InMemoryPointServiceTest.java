package io.hhplus.tdd;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.InMemoryPointService;
import io.hhplus.tdd.point.PointService;
import io.hhplus.tdd.point.UserPoint;

@DisplayName("PointService 테스트")
@ExtendWith(MockitoExtension.class)
public class InMemoryPointServiceTest {

	private PointService pointService;
	private UserPointTable userPointTable;

	@BeforeEach
	public void setUp() {
		userPointTable = Mockito.mock(UserPointTable.class);
		pointService = new InMemoryPointService(userPointTable);
	}

	@Test
	@DisplayName("유저 포인트 조회")
	void getUserPointTest() {
		// given
		long userId = 1L;
		UserPoint mockUserPoint = new UserPoint(userId, 1000L, System.currentTimeMillis());
		when(userPointTable.selectById(userId)).thenReturn(mockUserPoint);

		// when
		UserPoint getUserPoint = pointService.getPointById(userId);

		// then
		assertThat(mockUserPoint.point()).isEqualTo(getUserPoint.point());
	}

	@Test
	@DisplayName("유저 포인트 충전 성공")
	void chargePointSuccessTest() {
		// given
		long userId = 1L;
		UserPoint mockUserPoint = new UserPoint(userId, 7000L, System.currentTimeMillis());
		when(userPointTable.insertOrUpdate(userId, mockUserPoint.point() + 7000L))
			.thenReturn(new UserPoint(userId, 14000L, System.currentTimeMillis()));

		// when
		UserPoint userPoint = pointService.chargePoint(userId, mockUserPoint.point(), 7000L);

		// then
		assertThat(userPoint.point()).isEqualTo(14000L);
		verify(userPointTable).insertOrUpdate(userId, 14000L);
	}

	@Test
	@DisplayName("유저 포인트 사용 성공")
	void usePointSuccessTest() {
		// given
		long userId = 1L;
		UserPoint mockUserPoint = new UserPoint(userId, 10000L, System.currentTimeMillis());
		when(userPointTable.insertOrUpdate(userId, mockUserPoint.point() - 1000L))
			.thenReturn(new UserPoint(userId, 9000L, System.currentTimeMillis()));

		// when
		UserPoint userPoint = pointService.usePoint(userId, mockUserPoint.point(), 1000L);

		// then
		assertThat(userPoint.point()).isEqualTo(9000L);
		verify(userPointTable).insertOrUpdate(userId, 9000L);
	}
}
