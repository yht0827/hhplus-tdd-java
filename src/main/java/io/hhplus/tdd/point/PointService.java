package io.hhplus.tdd.point;

public interface PointService {

	UserPoint getPointById(long id);

	UserPoint chargePoint(long id, long balance, long amount);

	UserPoint usePoint(long id, long balance, long amount);
}
