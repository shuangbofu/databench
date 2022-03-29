package org.example.databench.web.config;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.example.databench.persistence.entity.base.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by shuangbofu on 2021/9/10 10:23 下午
 */
@Configuration
public class DaoContext {

    TransmittableThreadLocal<Long> workspaceIdThreadLocal = new TransmittableThreadLocal<>();
    TransmittableThreadLocal<Long> bizThreadLocal = new TransmittableThreadLocal<>();
    TransmittableThreadLocal<String> userThreadLocal = new TransmittableThreadLocal<>();

    {
        userThreadLocal.set("test");
        bizThreadLocal.remove();
        workspaceIdThreadLocal.remove();
    }

    public void setWorkspaceId(Long workspaceId) {
        workspaceIdThreadLocal.set(workspaceId);
    }

    public void removeAll() {
        bizThreadLocal.remove();
        workspaceIdThreadLocal.remove();
    }

    public void setBizId(Long bizId) {
        bizThreadLocal.set(bizId);
    }

    public DaoContext setUser(String user) {
        userThreadLocal.set(user);
        return this;
    }

    @Bean
    public UserHook userHook() {
        return new UserHook() {

            @Override
            public void insert(UserEntity userEntity) {
                String user = userThreadLocal.get();
                userEntity.setCreateBy(user);
                userEntity.setModifiedBy(user);
            }

            @Override
            public void defaultQuery(QueryWrapper<UserEntity> queryWrapper) {
            }

            @Override
            public void defaultUpdate(UpdateWrapper<UserEntity> updateWrapper) {
                updateWrapper.set("modified_by", userThreadLocal.get());
            }
        };
    }

    @Bean
    public WorkspaceHook workspaceHook() {

        return new WorkspaceHook() {
            @Override
            public void insert(WorkspaceEntity workspaceEntity) {
                consumer(workspaceEntity::setWorkspaceId);
            }

            @Override
            public void defaultQuery(QueryWrapper<WorkspaceEntity> queryWrapper) {
                consumer(i -> queryWrapper.eq("workspace_id", i));
            }

            @Override
            public void defaultUpdate(UpdateWrapper<WorkspaceEntity> updateWrapper) {
                consumer(i -> updateWrapper.eq("workspace_id", i));
            }

            private void consumer(Consumer<Long> consumer) {
                Optional.ofNullable(workspaceIdThreadLocal.get()).filter(i -> i != 0L)
                        .ifPresent(consumer);
            }
        };
    }

    @Bean
    public BizHook bizHook() {
        return new BizHook() {
            @Override
            public void insert(BizEntity bizEntity) {
                consumer(bizEntity::setBizId);
            }

            @Override
            public void defaultQuery(QueryWrapper<BizEntity> queryWrapper) {
                consumer(i -> queryWrapper.eq("biz_id", i));
            }

            @Override
            public void defaultUpdate(UpdateWrapper<BizEntity> updateWrapper) {
                consumer(i -> updateWrapper.eq("biz_id", i));
            }

            private void consumer(Consumer<Long> consumer) {
                Optional.ofNullable(bizThreadLocal.get()).filter(i -> i != 0L)
                        .ifPresent(consumer);
            }
        };
    }
}
