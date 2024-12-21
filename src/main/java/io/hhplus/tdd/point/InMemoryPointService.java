package io.hhplus.tdd.point;

import org.springframework.stereotype.Service;

import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InMemoryPointService implements PointService {

	private final UserPointTable userPointTable;

	@Override
	public UserPoint getPointById(long id) {
		return userPointTable.selectById(id);
	}

	@Override
	public UserPoint chargePoint(long id, long balance, long amount) {
		return userPointTable.insertOrUpdate(id, balance + amount);
	}

	@Override
	public UserPoint usePoint(long id, long balance, long amount) {
		return userPointTable.insertOrUpdate(id, balance - amount);
	}

}
