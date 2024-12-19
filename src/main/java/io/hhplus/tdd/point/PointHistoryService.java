package io.hhplus.tdd.point;

import java.util.List;

public interface PointHistoryService {

	List<PointHistory> getPointHistoryByUserId(long userId);

	PointHistory savePointHistory(long userId, long amount, TransactionType type, long updateMillis);
}
