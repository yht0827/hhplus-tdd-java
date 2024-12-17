package io.hhplus.tdd.point;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PointFacade {

	private final InMemoryPointService pointService;
	private final InMemoryPointValidationService pointValidationService;

	public UserPoint getPointById(long id) {
		return pointService.getPointById(id);
	}

	public List<PointHistory> getPointHistoryByUserId(long userId) {
		return pointService.getPointHistoryByUserId(userId);
	}

	public UserPoint chargePoint(long id, long amount) {
		long balance = getBalance(id);
		pointValidationService.chargePointValidation(balance, amount);

		return pointService.chargePoint(id, balance, amount);
	}

	public UserPoint usePoint(long id, long amount) {
		long balance = getBalance(id);
		pointValidationService.usePointValidation(balance, amount);

		return pointService.usePoint(id, balance, amount);
	}

	private long getBalance(long id) {
		return getPointById(id).point();
	}

}
