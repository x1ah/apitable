package com.vikadata.api.modular.appstore.model;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import cn.hutool.core.io.IoUtil;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.vikadata.api.FileHelper;
import com.vikadata.api.enterprise.appstore.enums.AppType;
import com.vikadata.api.enterprise.appstore.model.InstanceConfig;
import com.vikadata.api.enterprise.appstore.model.LarkInstanceConfig;
import com.vikadata.api.enterprise.appstore.model.LarkInstanceConfigProfile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Instance configuration
 */
public class AppInstanceConfigTest {

    @Test
    public void testLarkConfigFromString() {
        InputStream inputStream = FileHelper.getInputStreamFromResource("lark_config.json");
        String jsonString = IoUtil.read(inputStream, StandardCharsets.UTF_8);
        InstanceConfig config = LarkInstanceConfig.fromJsonString(jsonString);
        assertThat(config).isNotNull();
        assertThat(config.getType()).isEqualTo(AppType.LARK);
        assertThat(config.getProfile().getAppKey()).isNotBlank().isEqualTo("c123123");
    }

    @Test
    public void testLarkConfigToString() throws JSONException {
        LarkInstanceConfigProfile profile = new LarkInstanceConfigProfile("123456", "shag213123");
        LarkInstanceConfig config = new LarkInstanceConfig(profile);
        String data = config.toJsonString();
        String expected = "{\"profile\":{\"contactSyncDone\":false,\"eventCheck\":false,\"isConfigComplete\":false,\"appKey\":\"123456\",\"appSecret\":\"shag213123\"},\"type\":\"LARK\"}";
        JSONAssert.assertEquals(expected, data, false);
    }
}
