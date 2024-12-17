package io.hhplus.tdd.point;

import java.util.List;

import org.springframework.stereotype.Service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InMemoryPointService implements PointService {

	private final PointHistoryTable pointHistoryTable;
	private final UserPointTable userPointTable;

	@Override
	public UserPoint getPointById(long id) {
		return userPointTable.selectById(id);
	}

	@Override
	public List<PointHistory> getPointHistoryByUserId(long userId) {
		return pointHistoryTable.selectAllByUserId(userId);
	}

	@Override
	public UserPoint chargePoint(long id, long balance, long amount) {
		long chargePoint = calculateUpdatedBalance(balance, amount);

		UserPoint savedUserPoint = userPointTable.insertOrUpdate(id, chargePoint);
		pointHistoryTable.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

		return savedUserPoint;
	}

	@Override
	public UserPoint usePoint(long id, long balance, long amount) {
		long usePoint = calculateUpdatedBalance(balance, -amount);

		UserPoint savedUserPoint = userPointTable.insertOrUpdate(id, usePoint);
		pointHistoryTable.insert(id, amount, TransactionType.USE, System.currentTimeMillis());

		return savedUserPoint;
	}

	private long calculateUpdatedBalance(long balance, long amount) {
		return balance + amount;
	}
}
