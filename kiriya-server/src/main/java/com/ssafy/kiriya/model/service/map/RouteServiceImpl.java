package com.ssafy.kiriya.model.service.map;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;

import com.ssafy.kiriya.model.dao.AttractionDao;
import com.ssafy.kiriya.model.dao.RouteDao;
import com.ssafy.kiriya.model.dto.PlanAttraction;
import com.ssafy.kiriya.model.dto.map.Route;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
	private final AttractionDao attractionDao;
	private final RouteDao routeDao;

	@Override
	public void addAll(int pno, List<Route> route) {
		try {
			for (int order=0; order<route.size(); order++) {
				Route r = route.get(order);
				int ano = attractionDao.getAnoByContentId(r.getContentId());
				routeDao.add(PlanAttraction.builder()
						.pno(pno)
						.ano(ano)
						.sequenceOrder(order)
						.startDate(r.getStartDate())
						.endDate(r.getEndDate())
						.build());
			}
        } catch (PersistenceException e) {
        	e.printStackTrace();
        	throw new IllegalArgumentException("Route 추가 실패! : " + route);
        }
	}

	@Override
	public List<Route> getByPno(int pno) {
		try {
			List<Route> route = routeDao.getByPno(pno);
			return route;
	    } catch (PersistenceException e) {
	    	e.printStackTrace();
	    	throw new IllegalArgumentException("Route 조회 실패! : " + pno);
	    }
	}

	@Override
	public void removeAll(int pno) {
		routeDao.removeAll(pno);
	}
	
}
