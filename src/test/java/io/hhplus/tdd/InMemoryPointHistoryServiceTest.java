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
import io.hhplus.tdd.point.InMemoryPointHistoryService;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.PointHistoryService;

@DisplayName("PointService 테스트")
@ExtendWith(MockitoExtension.class)
public class InMemoryPointHistoryServiceTest {

	private PointHistoryTable pointHistoryTable;
	private PointHistoryService pointHistoryService;

	@BeforeEach
	public void setUp() {
		pointHistoryTable = Mockito.mock(PointHistoryTable.class);
		pointHistoryService = new InMemoryPointHistoryService(pointHistoryTable);
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
		List<PointHistory> userHistories = pointHistoryService.getPointHistoryByUserId(userId);

		//then
		assertThat(userHistories).hasSize(3);
		assertThat(userHistories.get(0).amount()).isEqualTo(10000);
		assertThat(userHistories.get(0).type()).isEqualTo(CHARGE);
		assertThat(userHistories.get(1).amount()).isEqualTo(6000L);
		assertThat(userHistories.get(1).type()).isEqualTo(USE);
		assertThat(userHistories.get(2).amount()).isEqualTo(1000L);
		assertThat(userHistories.get(2).type()).isEqualTo(USE);
	}
}
