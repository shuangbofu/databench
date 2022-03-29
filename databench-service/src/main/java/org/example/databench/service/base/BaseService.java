package org.example.databench.service.base;

import io.github.shuangbofu.helper.entity.IdEntity;
import io.github.shuangbofu.helper.handler.QueryHandler;
import io.github.shuangbofu.helper.handler.UpdateHandler;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Created by shuangbofu on 2021/9/10 11:00 下午
 */
public interface BaseService<ENTITY extends IdEntity> {
    ENTITY selectOneById(Long id);

    ENTITY selectOneOrThrow(Long id);

    long countAll();

    boolean deleteById(Long id);

    boolean updateBy(UpdateHandler<ENTITY> updateHandler);

    boolean updateById(Long id, UpdateHandler<ENTITY> updateHandler);

    boolean updateEntityById(ENTITY entity);

    ENTITY selectOneBy(QueryHandler<ENTITY> handler);

    List<ENTITY> selectListBy(QueryHandler<ENTITY> handler);


    /*
        泛型可变参数会报警告⚠️所以使用方法重载
     */
    <PARAM> boolean insertAny(PARAM param, BiConsumer<PARAM, ENTITY> customMappers);

    <PARAM> boolean insertAny(PARAM param);

    <PARAM> ENTITY insertAnyRtEntity(PARAM param);

    <PARAM> ENTITY insertAnyRtEntity(PARAM param, BiConsumer<PARAM, ENTITY> customMappers);

    <PARAM> Long insertAnyRtId(PARAM param);
}
