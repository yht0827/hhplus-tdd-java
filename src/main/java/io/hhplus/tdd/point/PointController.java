package io.hhplus.tdd.point;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/point")
public class PointController {

	private static final Logger log = LoggerFactory.getLogger(PointController.class);
	private final PointFacade pointFacade;

	@GetMapping("{id}")
	public UserPoint point(
		@PathVariable long id
	) {
		return pointFacade.getPointById(id);
	}

	@GetMapping("{id}/histories")
	public List<PointHistory> history(
		@PathVariable long id
	) {
		return pointFacade.getPointHistoryByUserId(id);
	}

	@PatchMapping("{id}/charge")
	public UserPoint charge(
		@PathVariable long id,
		@RequestBody long amount
	) {
		return pointFacade.chargePoint(id, amount);
	}

	@PatchMapping("{id}/use")
	public UserPoint use(
		@PathVariable long id,
		@RequestBody long amount
	) {
		return pointFacade.usePoint(id, amount);
	}
}
