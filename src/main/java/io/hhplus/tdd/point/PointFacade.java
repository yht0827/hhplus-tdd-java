package io.hhplus.tdd.point;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PointFacade {

	private final PointService pointService;
	private final PointValidationService pointValidationService;
	private final PointHistoryService pointHistoryService;

	public UserPoint getPointById(long id) {
		return pointService.getPointById(id);
	}

	public List<PointHistory> getPointHistoryByUserId(long userId) {
		return pointHistoryService.getPointHistoryByUserId(userId);
	}

	public UserPoint chargePoint(long id, long amount) {
		long balance = getBalance(id);

		// 1. validation
		pointValidationService.chargePointValidation(balance, amount);

		// 2. save Point
		UserPoint userPoint = pointService.chargePoint(id, balance, amount);

		// 3. save history
		pointHistoryService.savePointHistory(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

		return userPoint;
	}

	public UserPoint usePoint(long id, long amount) {
		long balance = getBalance(id);

		// 1. validation
		pointValidationService.usePointValidation(balance, amount);

		// 2. save Point
		UserPoint userPoint = pointService.usePoint(id, balance, amount);

		// 3. save history
		pointHistoryService.savePointHistory(id, amount, TransactionType.USE, System.currentTimeMillis());

		return userPoint;
	}

	private long getBalance(long id) {
		return getPointById(id).point();
	}
}
