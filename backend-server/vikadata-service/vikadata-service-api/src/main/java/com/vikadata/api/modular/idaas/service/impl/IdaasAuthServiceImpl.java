package com.vikadata.api.modular.idaas.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.net.URLEncoder;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import com.vikadata.api.component.TaskManager;
import com.vikadata.api.context.SessionContext;
import com.vikadata.api.enums.action.TrackEventType;
import com.vikadata.api.enums.exception.IdaasException;
import com.vikadata.api.model.dto.client.ClientOriginInfo;
import com.vikadata.api.modular.base.service.SensorsService;
import com.vikadata.api.modular.idaas.model.IdaasAuthLoginVo;
import com.vikadata.api.modular.idaas.service.IIdaasAppBindService;
import com.vikadata.api.modular.idaas.service.IIdaasAppService;
import com.vikadata.api.modular.idaas.service.IIdaasAuthService;
import com.vikadata.api.modular.idaas.service.IIdaasUserBindService;
import com.vikadata.api.modular.organization.service.IMemberService;
import com.vikadata.api.modular.user.service.IUserService;
import com.vikadata.api.util.InformationUtil;
import com.vikadata.boot.autoconfigure.idaas.IdaasProperties;
import com.vikadata.core.exception.BusinessException;
import com.vikadata.entity.IdaasAppBindEntity;
import com.vikadata.entity.IdaasAppEntity;
import com.vikadata.entity.IdaasUserBindEntity;
import com.vikadata.entity.MemberEntity;
import com.vikadata.integration.idaas.IdaasApiException;
import com.vikadata.integration.idaas.IdaasTemplate;
import com.vikadata.integration.idaas.api.AuthApi;
import com.vikadata.integration.idaas.constant.UrlConstant;
import com.vikadata.integration.idaas.model.AccessTokenRequest;
import com.vikadata.integration.idaas.model.AccessTokenResponse;
import com.vikadata.integration.idaas.model.UserInfoResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Service;

/**
 * <p>
 * IDaaS Login authorization
 * </p>
 */
@Slf4j
@Service
public class IdaasAuthServiceImpl implements IIdaasAuthService {

    private static final String VIKA_LOGIN_URL = "{host}{contextPath}/idaas/auth/login/redirect/{clientId}";

    private static final String VIKA_CALLBACK_URL = "{host}/user/idaas/callback?client_id={clientId}";

    private static final String VIKA_SPACE_CALLBACK_URL = "{host}/user/idaas/callback?client_id={clientId}&space_id={spaceId}";

    @Autowired(required = false)
    private IdaasProperties idaasProperties;

    @Resource
    private ServerProperties serverProperties;

    @Autowired(required = false)
    private IdaasTemplate idaasTemplate;

    @Resource
    private IIdaasAppService idaasAppService;

    @Resource
    private IIdaasAppBindService idaasAppBindService;

    @Resource
    private IIdaasUserBindService idaasUserBindService;

    @Resource
    private IMemberService memberService;

    @Resource
    private SensorsService sensorsService;

    @Resource
    private IUserService userService;

    @Override
    public String getVikaLoginUrl(String clientId) {
        Dict variable = Dict.create()
                .set("host", idaasProperties.getServerHost())
                .set("contextPath", serverProperties.getServlet().getContextPath())
                .set("clientId", clientId);

        return StrUtil.format(VIKA_LOGIN_URL, variable);
    }

    @Override
    public String getVikaCallbackUrl(String clientId, String spaceId) {
        Dict variable = Dict.create()
                .set("host", idaasProperties.getServerHost())
                .set("clientId", clientId);

        String url;
        if (CharSequenceUtil.isBlank(spaceId)) {
            url = StrUtil.format(VIKA_CALLBACK_URL, variable);
        }
        else {
            variable.set("spaceId", spaceId);

            url = StrUtil.format(VIKA_SPACE_CALLBACK_URL, variable);
        }

        return url;
    }

    @Override
    public IdaasAuthLoginVo idaasLoginUrl(String clientId) {
        IdaasAppEntity appEntity = idaasAppService.getByClientId(clientId);
        if (Objects.isNull(appEntity)) {
            throw new BusinessException(IdaasException.APP_NOT_FOUND);
        }

        String redirectUri = URLEncoder.ALL.encode(getVikaCallbackUrl(clientId, null), StandardCharsets.UTF_8);
        String loginUrl = UrlConstant.getAuthorizationUrl(appEntity.getAuthorizationEndpoint(),
                clientId, redirectUri, UUID.randomUUID().toString(true));

        return IdaasAuthLoginVo.builder()
                .loginUrl(loginUrl)
                .build();
    }

    @Override
    public void idaasLoginCallback(String clientId, String spaceId, String authCode, String state) {
        if (CharSequenceUtil.isBlank(spaceId) && !idaasProperties.isSelfHosted()) {
            // Non private deployment spaceId cannot be empty
            throw new BusinessException(IdaasException.PARAM_INVALID);
        }
        IdaasAppEntity appEntity = idaasAppService.getByClientId(clientId);
        if (Objects.isNull(appEntity)) {
            throw new BusinessException(IdaasException.APP_NOT_FOUND);
        }
        if (CharSequenceUtil.isNotBlank(spaceId)) {
            IdaasAppBindEntity appBindEntity = idaasAppBindService.getBySpaceId(spaceId);
            if (Objects.isNull(appBindEntity)) {
                throw new BusinessException(IdaasException.APP_SPACE_NOT_BIND);
            }
            else if (!clientId.equals(appBindEntity.getClientId())) {
                throw new BusinessException(IdaasException.APP_SPACE_INVALID_BIND);
            }
        }

        AuthApi authApi = idaasTemplate.getAuthApi();
        // 1 Exchange the code returned by callback for user information access_ token
        AccessTokenResponse accessTokenResponse;
        try {
            // redirectUri, the parameters are exactly the same as those previously logged in, and can be directly re spliced
            String redirectUri = URLEncoder.ALL.encode(getVikaCallbackUrl(clientId, spaceId), StandardCharsets.UTF_8);
            AccessTokenRequest accessTokenRequest = new AccessTokenRequest();
            accessTokenRequest.setCode(authCode);
            accessTokenRequest.setRedirectUri(redirectUri);
            accessTokenResponse = authApi.accessToken(appEntity.getTokenEndpoint(), clientId, appEntity.getClientSecret(), accessTokenRequest);
        }
        catch (IdaasApiException ex) {
            log.warn("Failed to get access token.", ex);

            throw new BusinessException(IdaasException.API_ERROR);
        }
        // 2 Through access_ Token to obtain user information
        UserInfoResponse userInfoResponse;
        try {
            userInfoResponse = authApi.userInfo(appEntity.getUserinfoEndpoint(), accessTokenResponse.getAccessToken());
        }
        catch (IdaasApiException ex) {
            log.warn("Failed to get user info.", ex);

            throw new BusinessException(IdaasException.API_ERROR);
        }
        // 3 Get associated user information
        IdaasUserBindEntity userBind = idaasUserBindService.getByUserId(userInfoResponse.getUserId());
        if (Objects.isNull(userBind)) {
            throw new BusinessException(IdaasException.USER_NOT_BIND);
        }
        // 4 Get member information and activate
        if (CharSequenceUtil.isBlank(spaceId)) {
            List<MemberEntity> memberEntities = memberService.getByUserId(userBind.getVikaUserId());
            if (CollUtil.isEmpty(memberEntities)) {
                throw new BusinessException(IdaasException.MEMBER_NOT_BIND);
            }

            memberEntities.forEach(memberEntity -> {
                if (Boolean.FALSE.equals(memberEntity.getIsActive())) {
                    // 4.1 If the member is not activated, change to the activated state
                    memberService.updateById(MemberEntity.builder()
                            .id(memberEntity.getId())
                            .isActive(true)
                            .build());
                }
            });
        }
        else {
            MemberEntity memberEntity = memberService.getByUserIdAndSpaceId(userBind.getVikaUserId(), spaceId);
            if (Objects.isNull(memberEntity)) {
                throw new BusinessException(IdaasException.MEMBER_NOT_BIND);
            }

            if (Boolean.FALSE.equals(memberEntity.getIsActive())) {
                // 4.1 If the member is not activated, change to the activated state
                memberService.updateById(MemberEntity.builder()
                        .id(memberEntity.getId())
                        .isActive(true)
                        .build());
            }
        }

        // 5 Automatic logon
        SessionContext.setUserId(userBind.getVikaUserId());
        userService.updateLoginTime(userBind.getVikaUserId());
        // 6 Shence burial site
        ClientOriginInfo origin = InformationUtil.getClientOriginInfo(false, true);
        TaskManager.me().execute(() -> sensorsService
                .track(userBind.getVikaUserId(), TrackEventType.LOGIN, "IDaaS Single sign on", origin));
    }

}
