package com.ssafy.kiriya.model.service.trip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.kiriya.model.dao.PlanDao;
import com.ssafy.kiriya.model.dao.TripDao;
import com.ssafy.kiriya.model.dto.Plan;
import com.ssafy.kiriya.model.dto.Trip;
import com.ssafy.kiriya.model.dto.map.Route;
import com.ssafy.kiriya.model.service.map.RouteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {
	private final TripDao tripDao;	
	private final PlanDao planDao;
	private final RouteService routeService;


	@Override
	public void add(Trip trip) {
		tripDao.add(trip);
	}

	@Override
	public Trip get(int tno) {
		Trip trip = tripDao.get(tno);
		if (trip == null) {
			throw new IllegalArgumentException("조회된 여행이 없습니다. tno: "+tno);			
		}
		return trip;
	}
	
	@Override
	public List<Trip> getAll() {
		return tripDao.getAll();
	}

	@Override
	public List<Trip> getByMno(int mno) {
		return tripDao.getByMno(mno);
	}

	@Override
	public void update(Trip trip) {
		tripDao.set(trip);
	}

	@Override
	public void delete(int tno) {
		tripDao.remove(tno);
	}

	@Override
	public void addTripByCopy(int originalTno, int gno) {
		Map<String, Object> param = new HashMap<>();
		param.put("originalTno", originalTno);
		param.put("gno", gno);
		tripDao.copyTrip(param);
		Integer newTno = (Integer) param.get("tno");
		List<Plan> originalPlans = planDao.getByTno(originalTno);
		for(Plan p : originalPlans) {
			int originalPno = p.getPno();
			p.setPno(0);
			p.setTno(newTno);
			planDao.add(p);
			int newPno = p.getPno();
			
			List<Route> originalRoutes = routeService.getByPno(originalPno);
			routeService.addAll(newPno, originalRoutes);
		}
	}

	@Override
	public List<Trip> getByGroup(int gno) {
		return tripDao.getByGno(gno);
	}
}
