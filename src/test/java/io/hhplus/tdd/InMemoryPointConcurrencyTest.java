package io.hhplus.tdd;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.hhplus.tdd.point.PointService;
import io.hhplus.tdd.point.UserPoint;

@SpringBootTest
public class InMemoryPointConcurrencyTest {

	private static final ExecutorService executorService = Executors.newFixedThreadPool(30);
	private static final int THREAD_COUNT = 10;

	@Autowired
	private PointService pointService;

	@DisplayName("동시에 10명 포인트 충전 요청")
	@Test
	void chargePointConcurrentTest() throws InterruptedException {
		long userId = 1L;
		long amount = 100L;

		CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
		for (int i = 0; i < THREAD_COUNT; i++) {
			executorService.submit(() -> {
				try {
					UserPoint userPoint = pointService.getPointById(userId);
					pointService.chargePoint(userId, userPoint.point(), amount);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executorService.shutdown();

		UserPoint userPoint = pointService.getPointById(userId);
		assertThat(userPoint.point()).isEqualTo(1000L);
	}

	@DisplayName("동시에 10명 포인트 사용")
	@Test
	void usePointConcurrentTest() throws InterruptedException {
		long userId = 1L;
		long amount = 100L;
		CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

		pointService.chargePoint(userId, 0L, 1000L);

		for (int i = 0; i < THREAD_COUNT; i++) {
			executorService.submit(() -> {
				try {
					UserPoint userPoint = pointService.getPointById(userId);
					pointService.usePoint(userId, userPoint.point(), amount);
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();
		executorService.shutdown();

		UserPoint userPoint = pointService.getPointById(userId);
		assertThat(userPoint.point()).isZero();

	}
}
