package io.hhplus.tdd.point;

import java.util.List;

public interface PointService {

	UserPoint getPointById(long id);

	List<PointHistory> getPointHistoryByUserId(long userId);

	UserPoint chargePoint(long id, long balance, long amount);

	UserPoint usePoint(long id, long balance, long amount);
}
