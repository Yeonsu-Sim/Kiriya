package com.ssafy.kiriya.model.service.plan;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.kiriya.model.dao.PlanDao;
import com.ssafy.kiriya.model.dto.Plan;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanDao planDao;

    @Override
    @Transactional
    public int add(Plan plan) {
        return planDao.add(plan);
    }

    @Override
    public Plan getByPno(int pno) {
        return planDao.getByPno(pno);
    }

    @Override
    public List<Plan> getByTno(int tno) {
        return planDao.getByTno(tno);
    }

    @Override
    public int set(Plan plan) {
        return planDao.set(plan);
    }

    @Override
    public int remove(int pno) {
        return planDao.remove(pno);
    }

}
