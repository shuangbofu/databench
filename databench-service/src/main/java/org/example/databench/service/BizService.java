package org.example.databench.service;

import org.example.databench.persistence.entity.Biz;
import org.example.databench.service.base.BaseService;
import org.example.databench.service.domain.param.BizParam;
import org.example.databench.service.domain.vo.BizVO;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/11 9:27 下午
 */
public interface BizService extends BaseService<Biz> {
    List<BizVO> selectAllBizs();

    Long addBiz(BizParam bizBaseParam);
}
