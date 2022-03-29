package org.example.databench.service.impl;

import org.example.databench.persistence.dao.BizDao;
import org.example.databench.persistence.entity.Biz;
import org.example.databench.service.BizService;
import org.example.databench.service.base.CommonService;
import org.example.databench.service.domain.param.BizParam;
import org.example.databench.service.domain.vo.BizVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/11 9:28 下午
 */
@Service
public class BizServiceImpl extends CommonService<Biz, BizDao> implements BizService {
    public BizServiceImpl(@Autowired BizDao dao) {
        super(dao);
    }

    @Override
    public List<BizVO> selectAllBizs() {
        return listAToListB(selectAll(), BizVO.class);
    }

    @Override
    public Long addBiz(BizParam bizParam) {
        return insertAnyRtId(bizParam);
    }
}
