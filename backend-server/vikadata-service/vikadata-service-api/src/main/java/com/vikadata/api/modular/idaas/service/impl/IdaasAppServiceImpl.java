package com.vikadata.api.modular.idaas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.vikadata.api.modular.idaas.mapper.IdaasAppMapper;
import com.vikadata.api.modular.idaas.service.IIdaasAppService;
import com.vikadata.entity.IdaasAppEntity;

import org.springframework.stereotype.Service;

/**
 * <p>
 * IDaaS application information
 * </p>
 */
@Service
public class IdaasAppServiceImpl extends ServiceImpl<IdaasAppMapper, IdaasAppEntity> implements IIdaasAppService {

    @Override
    public IdaasAppEntity getByClientId(String clientId) {
        return getBaseMapper().selectByClientId(clientId);
    }

}
