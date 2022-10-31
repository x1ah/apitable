package com.vikadata.api.modular.control.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.extern.slf4j.Slf4j;

import com.vikadata.api.control.ControlType;
import com.vikadata.api.enums.exception.DatabaseException;
import com.vikadata.api.modular.control.mapper.ControlMapper;
import com.vikadata.api.modular.control.service.IControlRoleService;
import com.vikadata.api.modular.control.service.IControlService;
import com.vikadata.api.modular.control.service.IControlSettingService;
import com.vikadata.api.modular.organization.service.IMemberService;
import com.vikadata.core.util.ExceptionUtil;
import com.vikadata.core.util.SqlTool;
import com.vikadata.entity.ControlEntity;
import com.vikadata.entity.MemberEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Control service implementation class
 */
@Service
@Slf4j
public class ControlServiceImpl extends ServiceImpl<ControlMapper, ControlEntity> implements IControlService {

    @Resource
    private ControlMapper controlMapper;

    @Resource
    private IControlRoleService iControlRoleService;

    @Resource
    private IControlSettingService iControlSettingService;

    @Resource
    private IMemberService iMemberService;

    @Override
    public ControlEntity getByControlId(String controlId) {
        log.info("Query control permission unit information");
        return controlMapper.selectByControlId(controlId);
    }

    @Override
    public void checkControlStatus(String controlId, Consumer<Boolean> consumer) {
        log.info("Check the status of authority control unit「{}」", controlId);
        int count = SqlTool.retCount(controlMapper.selectCountByControlId(controlId));
        consumer.accept(count > 0);
    }

    @Override
    public void create(Long userId, String spaceId, String controlId, ControlType controlType) {
        log.info("Create permission control unit.userId:{},spaceId:{},controlId:{}", userId, spaceId, controlId);
        ControlEntity deletedEntity = controlMapper.selectDeletedByControlIdAndSpaceId(controlId, spaceId, controlType);
        boolean flag;
        if (deletedEntity != null) {
            deletedEntity.setIsDeleted(false);
            deletedEntity.setUpdatedBy(userId);
            flag =
                    SqlHelper.retBool(controlMapper.updateIsDeletedByIds(Collections.singletonList(deletedEntity.getId()), userId, false));
        }
        else {
            ControlEntity entity = ControlEntity.builder()
                    .id(IdWorker.getId())
                    .spaceId(spaceId)
                    .controlId(controlId)
                    .controlType(controlType.getVal())
                    .createdBy(userId)
                    .updatedBy(userId)
                    .build();
            flag = SqlHelper.retBool(controlMapper.insertBatch(Collections.singletonList(entity)));
        }
        ExceptionUtil.isTrue(flag, DatabaseException.INSERT_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeControl(Long userId, List<String> controlIds, boolean delSetting) {
        log.info("Delete the specified control unit「{}」", controlIds);
        boolean flag = SqlHelper.retBool(controlMapper.deleteByControlIds(userId, controlIds));
        ExceptionUtil.isTrue(flag, DatabaseException.DELETE_ERROR);
        // Delete all roles of the specified control unit (no role may exist)
        iControlRoleService.removeByControlIds(userId, controlIds);
        // Delete the specified control unit settings
        if (delSetting) {
            iControlSettingService.removeByControlIds(userId, controlIds);
        }
    }

    @Override
    public List<String> getControlIdByControlIdPrefixAndType(String prefix, Integer type) {
        return controlMapper.selectControlIdByControlIdPrefixAndType(prefix, type);
    }

    @Override
    public List<String> getExistedControlId(List<String> controlIds) {
        return controlMapper.selectControlIdByControlIds(controlIds);
    }

    @Override
    public Long getOwnerMemberId(String controlId) {
        log.info("Get the member ID of the owner of permission control unit 「{}」", controlId);
        ControlEntity controlEntity = controlMapper.selectByControlId(controlId);
        if (controlEntity == null) {
            return null;
        }
        if (controlEntity.getUpdatedBy() == null) {
            return null;
        }
        MemberEntity memberEntity = iMemberService.getByUserIdAndSpaceId(controlEntity.getUpdatedBy(), controlEntity.getSpaceId());
        if (memberEntity == null) {
            return null;
        }
        return memberEntity.getId();
    }
}
