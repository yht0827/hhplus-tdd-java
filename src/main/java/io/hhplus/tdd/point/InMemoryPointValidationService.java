package io.hhplus.tdd.point;

import org.springframework.stereotype.Component;

@Component
public class InMemoryPointValidationService implements PointValidationService {

	private static final long MAX_BALANCE = 100_000_000L;
	private static final long ONCE_MAX_CHARGE = 10_000L;
	private static final long MIN_CHARGE = 1000L;

	@Override
	public void chargePointValidation(long balance, long amount) {

		if (amount <= 0L)
			throw new IllegalArgumentException("충전 금액은 0 보다 커야합니다.");

		if (amount < MIN_CHARGE)
			throw new IllegalArgumentException("최소 충전 금액은 1000 입니다.");

		if (amount > ONCE_MAX_CHARGE)
			throw new IllegalArgumentException("한번에 충전 가능한 금액을 초과했습니다.");

		if (balance + amount < 0)
			throw new IllegalArgumentException("비정상적인 금액을 충전하려고 합니다.");

		if (balance + amount > MAX_BALANCE)
			throw new IllegalArgumentException("최대 잔고를 초과해서 충전할 수 없습니다.");
	}

	@Override
	public void usePointValidation(long balance, long amount) {

		if (amount <= 0L)
			throw new IllegalArgumentException("0 이하의 금액은 사용 할 수 없습니다.");

		if (balance - amount < 0L)
			throw new IllegalArgumentException("잔고가 부족합니다.");
	}
}
