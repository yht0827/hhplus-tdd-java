package io.hhplus.tdd;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.hhplus.tdd.point.InMemoryPointValidationService;
import io.hhplus.tdd.point.PointValidationService;
import io.hhplus.tdd.point.UserPoint;

@DisplayName("PointValidationService 테스트")
@ExtendWith(MockitoExtension.class)
public class InMemoryPointValidationServiceTest {

	private PointValidationService pointValidationService;

	@BeforeEach
	void setUp() {
		pointValidationService = new InMemoryPointValidationService();
	}

	@DisplayName("충전 금액 0보다 작아서 충전 실패")
	@Test
	void chargeFailWhenMoreThanZero() {
		// given
		long userId = 1;
		long amount = -1L;
		UserPoint userPoint = new UserPoint(userId, 1000L, 0);

		// when
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> pointValidationService.chargePointValidation(userPoint.point(), amount));

		// then
		assertThat("충전 금액은 0 보다 커야합니다.")
			.isEqualTo(exception.getMessage());
	}

	@DisplayName("최소 충전 금액 1000보다 작아서 충전 실패")
	@Test
	void chargeFailMinPoint() {
		// given
		long userId = 1;
		long amount = 999L;
		UserPoint userPoint = new UserPoint(userId, 1000L, 0);

		// when
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> pointValidationService.chargePointValidation(userPoint.point(), amount));

		// then
		assertThat("최소 충전 금액은 1000 입니다.")
			.isEqualTo(exception.getMessage());

	}

	@DisplayName("한번에 충전 가능한 금액을 초과해서 충전 실패")
	@Test
	void chargeFailOnceMaxPoint() {
		// given
		long userId = 1;
		long amount = 10_001L;
		UserPoint userPoint = new UserPoint(userId, 1000L, 0);

		// when
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> pointValidationService.chargePointValidation(userPoint.point(), amount));

		// then
		assertThat("한번에 충전 가능한 금액을 초과했습니다.")
			.isEqualTo(exception.getMessage());
	}

	@DisplayName("비정상적인 금액을 충전해서 실패")
	@Test
	void chargeFailStrangeAmount() {
		// given
		long userId = 1;
		long amount = 9999L;
		UserPoint userPoint = new UserPoint(userId, -10_000L, 0);

		// when
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> pointValidationService.chargePointValidation(userPoint.point(), amount));

		// then
		assertThat("비정상적인 금액을 충전하려고 합니다.")
			.isEqualTo(exception.getMessage());
	}

	@DisplayName("최대 잔고를 초과하여 충전 실패")
	@Test
	void chargeFailMaxPoint() {
		// given
		long userId = 1;
		long amount = 1000L;
		UserPoint userPoint = new UserPoint(userId, 99_999_999L, 0);

		// when
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> pointValidationService.chargePointValidation(userPoint.point(), amount));

		// then
		assertThat("최대 잔고를 초과해서 충전할 수 없습니다.")
			.isEqualTo(exception.getMessage());
	}

	@DisplayName("0 이하의 금액을 사용 할 수 없어서 포인트 사용 실패")
	@Test
	void useFailLessThanZeroPoint() {
		// given
		long userId = 1;
		long amount = -11L;
		UserPoint userPoint = new UserPoint(userId, 99_999_999L, 0);

		// when
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> pointValidationService.usePointValidation(userPoint.point(), amount));

		// then
		assertThat("0 이하의 금액은 사용 할 수 없습니다.")
			.isEqualTo(exception.getMessage());
	}

	@DisplayName("잔고가 부족하여 포인트 사용 실패")
	@Test
	void notEnoughBalance() {
		// given
		long userId = 1;
		long amount = 6000L;
		UserPoint userPoint = new UserPoint(userId, 5000L, 0);

		// when
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> pointValidationService.usePointValidation(userPoint.point(), amount));

		// then
		assertThat("잔고가 부족합니다.")
			.isEqualTo(exception.getMessage());
	}
}
