package com.ssafy.kiriya.model.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.kiriya.model.dto.Plan;

@Mapper
public interface PlanDao {
	
    int add(Plan plan);
    
    Plan getByPno(int pno);
    
    List<Plan> getByTno(int tno);
    
    int set(Plan plan);
    
    int remove(int pno);
    
}