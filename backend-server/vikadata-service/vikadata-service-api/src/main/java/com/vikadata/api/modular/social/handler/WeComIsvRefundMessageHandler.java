package com.vikadata.api.modular.social.handler;

import java.util.Map;

import javax.annotation.Resource;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.bean.message.WxCpTpXmlMessage;
import me.chanjar.weixin.cp.bean.message.WxCpXmlOutMessage;
import me.chanjar.weixin.cp.tp.service.WxCpTpService;

import com.vikadata.api.modular.social.enums.SocialCpIsvMessageProcessStatus;
import com.vikadata.api.modular.social.service.ISocialCpIsvMessageService;
import com.vikadata.entity.SocialCpIsvMessageEntity;
import com.vikadata.social.wecom.constants.WeComIsvMessageType;
import com.vikadata.social.wecom.handler.WeComIsvMessageHandler;
import com.vikadata.social.wecom.model.WxCpIsvXmlMessage;

import org.springframework.stereotype.Component;

/**
 * <p>
 * Refund notification for authorized enterprise applications by third-party service providers
 * </p>
 */
@Component
@Slf4j
public class WeComIsvRefundMessageHandler implements WeComIsvMessageHandler {

    @Resource
    private ISocialCpIsvMessageService socialCpIsvMessageService;

    @Override
    public WeComIsvMessageType messageType() {

        return WeComIsvMessageType.REFUND;

    }

    @Override
    public WxCpXmlOutMessage handle(WxCpTpXmlMessage wxMessage, Map<String, Object> context, WxCpTpService wxCpService, WxSessionManager sessionManager) throws WxErrorException {
        WxCpIsvXmlMessage wxCpIsvXmlMessage = (WxCpIsvXmlMessage) wxMessage;
        SocialCpIsvMessageEntity entity = SocialCpIsvMessageEntity.builder()
                .type(WeComIsvMessageType.REFUND.getType())
                .suiteId(wxCpIsvXmlMessage.getSuiteId())
                .infoType(WeComIsvMessageType.REFUND.getInfoType())
                .authCorpId(wxCpIsvXmlMessage.getPaidCorpId())
                .timestamp(Long.parseLong(wxCpIsvXmlMessage.getTimeStamp()))
                .message(JSONUtil.toJsonStr(wxCpIsvXmlMessage))
                .processStatus(SocialCpIsvMessageProcessStatus.PENDING.getValue())
                .build();
        socialCpIsvMessageService.save(entity);

        socialCpIsvMessageService.sendToMq(entity.getId(), entity.getInfoType(), entity.getAuthCorpId(), entity.getSuiteId());

        return null;
    }

}
