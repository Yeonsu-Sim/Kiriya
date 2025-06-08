package com.ssafy.kiriya.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.kiriya.model.dto.PlanAttraction;
import com.ssafy.kiriya.model.dto.map.Route;

@Mapper
public interface RouteDao {
	
    int add(PlanAttraction planAttraction);
    
    Route getByNo(int no);
    
    List<Route> getByPno(int pno);
    
    int set(PlanAttraction planAttraction);
    
    int remove(int no);
    
    int removeAll(int pno);
}
