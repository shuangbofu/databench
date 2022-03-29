package org.example.databench.service.biz;

import org.example.databench.service.domain.node.Graph;
import org.example.databench.service.domain.vo.FileNodeVO;

/**
 * Created by shuangbofu on 2021/9/11 9:34 下午
 */
public interface BusinessProcessBizService {
    Graph<FileNodeVO> drawBizGraph();
}
