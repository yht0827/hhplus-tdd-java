package io.hhplus.tdd.point;

import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InMemoryLockService implements LockService {

	private final ReentrantLock lock = new ReentrantLock(true);

	@Override
	public void lock() {
		lock.lock();
	}

	@Override
	public void unlock() {
		lock.unlock();
	}
}
