package io.hhplus.tdd.point;

import java.util.List;

import org.springframework.stereotype.Service;

import io.hhplus.tdd.database.PointHistoryTable;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InMemoryPointHistoryService implements PointHistoryService {

	private final PointHistoryTable pointHistoryTable;

	@Override
	public List<PointHistory> getPointHistoryByUserId(long userId) {
		return pointHistoryTable.selectAllByUserId(userId);
	}

	@Override
	public PointHistory savePointHistory(long userId, long amount, TransactionType type, long updateMillis) {
		return pointHistoryTable.insert(userId, amount, type, System.currentTimeMillis());
	}

}
