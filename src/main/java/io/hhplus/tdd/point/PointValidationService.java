package io.hhplus.tdd.point;

public interface PointValidationService {

	void chargePointValidation(long balance, long amount);

	void usePointValidation(long balance, long amount);
}
