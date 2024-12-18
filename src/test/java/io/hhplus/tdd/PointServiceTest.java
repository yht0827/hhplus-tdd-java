package io.hhplus.tdd;

import static io.hhplus.tdd.point.TransactionType.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.InMemoryPointService;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.PointService;
import io.hhplus.tdd.point.UserPoint;

@DisplayName("PointService 테스트")
@ExtendWith(MockitoExtension.class)
public class PointServiceTest {

	private PointService pointService;
	private UserPointTable userPointTable;
	private PointHistoryTable pointHistoryTable;

	@BeforeEach
	public void setUp() {
		userPointTable = Mockito.mock(UserPointTable.class);
		pointHistoryTable = Mockito.mock(PointHistoryTable.class);
		pointService = new InMemoryPointService(pointHistoryTable, userPointTable);
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
	@DisplayName("유저 포인트 내역 조회")
	void getUserPointHistoryTest() {
		// given
		long userId = 1L;
		List<PointHistory> mockHistories = List.of(
			new PointHistory(1L, userId, 10000L, CHARGE, System.currentTimeMillis()),
			new PointHistory(2L, userId, 6000L, USE, System.currentTimeMillis()),
			new PointHistory(3L, userId, 1000L, USE, System.currentTimeMillis())
		);
		when(pointHistoryTable.selectAllByUserId(userId)).thenReturn(mockHistories);

		// when
		List<PointHistory> userHistories = pointService.getPointHistoryByUserId(userId);

		//then
		assertThat(userHistories).hasSize(3);
		assertThat(userHistories.get(0).amount()).isEqualTo(10000);
		assertThat(userHistories.get(0).type()).isEqualTo(CHARGE);
		assertThat(userHistories.get(1).amount()).isEqualTo(6000L);
		assertThat(userHistories.get(1).type()).isEqualTo(USE);
		assertThat(userHistories.get(2).amount()).isEqualTo(1000L);
		assertThat(userHistories.get(2).type()).isEqualTo(USE);
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
