package io.hhplus.tdd;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.hhplus.tdd.point.PointFacade;
import io.hhplus.tdd.point.UserPoint;

@DisplayName("포인트 동시성 테스트")
@SpringBootTest
public class InMemoryPointConcurrencyTest {

	private static final ExecutorService executorService = Executors.newFixedThreadPool(30);
	private static final int THREAD_COUNT = 10;

	@Autowired
	private PointFacade pointFacade;

	@DisplayName("동시에 10명 포인트 충전 요청")
	@Test
	void chargePointConcurrentTest() throws InterruptedException {
		long userId = 1L;
		long amount = 1000L;
		CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

		for (int i = 0; i < THREAD_COUNT; i++) {
			executorService.submit(() -> {
				try {
					pointFacade.chargePoint(userId, amount);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executorService.shutdown();

		UserPoint userPoint = pointFacade.getPointById(userId);
		assertThat(userPoint.point()).isEqualTo(10000L);
	}

	@DisplayName("동시에 10명 포인트 사용")
	@Test
	void usePointConcurrentTest() throws InterruptedException {
		long userId = 1L;
		long amount = 1000L;
		CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

		pointFacade.chargePoint(userId, 10000L);

		for (int i = 0; i < THREAD_COUNT; i++) {
			executorService.submit(() -> {
				try {
					pointFacade.usePoint(userId, amount);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executorService.shutdown();

		UserPoint userPoint = pointFacade.getPointById(userId);
		assertThat(userPoint.point()).isZero();
	}

	@Test
	@DisplayName("포인트 충전 및 사용 동시성 테스트")
	void pointChargeAndUseConcurrencyTest() throws InterruptedException {
		long userId = 1L;
		CountDownLatch latch = new CountDownLatch(1);

		executorService.submit(() -> {
			try {
				pointFacade.chargePoint(userId, 5000L);
				pointFacade.usePoint(userId, 3000L);
				pointFacade.usePoint(userId, 2000L); // 순서대로 처리 하면 0포인트
				pointFacade.chargePoint(userId, 1000L);
				pointFacade.chargePoint(userId, 3000L);
				pointFacade.usePoint(userId, 4000L);
				pointFacade.chargePoint(userId, 5000L);
				pointFacade.usePoint(userId, 4000L);
				pointFacade.chargePoint(userId, 6000L);
				pointFacade.usePoint(userId, 2000L);
			} finally {
				latch.countDown();
			}
		});

		latch.await();
		executorService.shutdown();

		UserPoint userPoint = pointFacade.getPointById(userId);
		assertThat(userPoint.point()).isEqualTo(5000L);
	}
}
