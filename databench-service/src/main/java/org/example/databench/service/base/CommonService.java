package org.example.databench.service.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.shuangbofu.helper.dao.BaseDao;
import io.github.shuangbofu.helper.entity.IdEntity;
import io.github.shuangbofu.helper.handler.QueryHandler;
import io.github.shuangbofu.helper.handler.UpdateHandler;
import org.example.databench.common.utils.ReflectUtils;
import org.example.databench.common.vo.PageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Created by shuangbofu on 2021/8/25 10:04 下午
 */
public abstract class CommonService<ENTITY extends IdEntity,
        DAO extends BaseDao<ENTITY, ? extends BaseMapper<ENTITY>>> extends AbstractService
        implements BaseService<ENTITY> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonService.class);
    private final DAO dao;
    private final Class<ENTITY> entityClass;

    public CommonService(DAO dao) {
        this.dao = dao;
        Type[] genericTypes = ReflectUtils.getSuperclassGenericTypes(getClass());
        entityClass = (Class<ENTITY>) genericTypes[0];
    }

    protected List<ENTITY> selectAll() {
        return dao.selectAll();
    }

    @Override
    public ENTITY selectOneById(Long id) {
        return dao.selectOneById(id);
    }

    @Override
    public ENTITY selectOneOrThrow(Long id) {
        return dao.selectOneOptionalById(id)
                .orElseThrow(() ->
                        new RuntimeException(entityClass.getSimpleName() + " Not exist")
                );
    }

    @Override
    public long countAll() {
        return dao.countAll();
    }

    @Override
    public boolean deleteById(Long id) {
        return dao.deleteById(id) == 1;
    }

    protected long countBy(QueryHandler<ENTITY> handler) {
        return dao.countBy(handler);
    }

    public boolean insert(ENTITY entity) {
        return dao.insert(entity) == 1;
    }

    public ENTITY insertRtEntity(ENTITY entity) {
        dao.insert(entity);
        return entity;
    }

    @Override
    public boolean updateBy(UpdateHandler<ENTITY> updateHandler) {
        return dao.updateBy(updateHandler) > 0;
    }

    @Override
    public boolean updateById(Long id, UpdateHandler<ENTITY> updateHandler) {
        return dao.updateById(id, updateHandler) > 0;
    }

    protected boolean updateEntityById(ENTITY entity, Long id) {
        return dao.updateEntityById(entity, id) == 1;
    }

    @Override
    public boolean updateEntityById(ENTITY entity) {
        return dao.updateEntityById(entity) == 1;
    }

    protected DAO getDao() {
        return dao;
    }

    @Override
    public ENTITY selectOneBy(QueryHandler<ENTITY> handler) {
        return dao.selectOneBy(handler);
    }

    @Override
    public List<ENTITY> selectListBy(QueryHandler<ENTITY> handler) {
        return dao.selectListBy(handler);
    }

    @SafeVarargs
    protected final <PARAM> boolean updateAnyById0(PARAM param, Long id, BiConsumer<PARAM, ENTITY>... customMappers) {
        if (param == null) {
            return false;
        }
        ENTITY entity = fromParam(param, customMappers);
        return updateEntityById(entity, id);
    }

    @SafeVarargs
    protected final <PARAM> boolean insertAny0(PARAM param, BiConsumer<PARAM, ENTITY>... customMappers) {
        return insert(fromParam(param, customMappers));
    }

    @SafeVarargs
    protected final <PARAM> ENTITY insertAnyRtEntity0(PARAM param, BiConsumer<PARAM, ENTITY>... customMappers) {
        ENTITY entity = fromParam(param, customMappers);
        insert(entity);
        return entity;
    }


    @SafeVarargs
    protected final <PARAM> ENTITY fromParam(PARAM param, BiConsumer<PARAM, ENTITY>... customMappers) {
        return aToB(param, entityClass, customMappers);
    }

    @SafeVarargs
    protected final <VO> PageVO<VO> toVOPage(int pageNum, int pageSize,
                                             QueryHandler<ENTITY> handler, Class<VO> clazz,
                                             BiConsumer<ENTITY, VO>... customMappers) {
        Page<ENTITY> entityPage = dao.selectPage(pageNum, pageSize, handler);
        PageVO<VO> voPage = new PageVO<>();
        voPage.setData(listAToListB(entityPage.getRecords(), clazz, customMappers));
        voPage.setPageNum(entityPage.getCurrent());
        voPage.setPageSize(entityPage.getTotal());
        voPage.setTotalPage(entityPage.getPages());
        return voPage;
    }

    /*
       --------------------------------------------------------------------------------------------------------
    */

    @Override
    public <PARAM> boolean insertAny(PARAM param, BiConsumer<PARAM, ENTITY> customMappers) {
        return insertAny0(param, customMappers);
    }

    @Override
    public <PARAM> ENTITY insertAnyRtEntity(PARAM param, BiConsumer<PARAM, ENTITY> customMappers) {
        return insertAnyRtEntity0(param, customMappers);
    }

    @Override
    public <PARAM> boolean insertAny(PARAM param) {
        return insertAny0(param);
    }

    @Override
    public <PARAM> ENTITY insertAnyRtEntity(PARAM param) {
        return insertAnyRtEntity0(param);
    }

    @Override
    public <PARAM> Long insertAnyRtId(PARAM param) {
        return insertAnyRtEntity(param).getId();
    }
}
