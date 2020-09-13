import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.GlobalCredentials;
import com.huaweicloud.sdk.core.exception.ClientRequestException;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.ecs.v2.EcsClient;
import com.huaweicloud.sdk.ecs.v2.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Edward Gavin
 * @Create: 2020-09-13 14:46
 */
@lombok.extern.slf4j.Slf4j
public class Application {
    // 访问密钥
    private static String ak = "5A9TP9JPZEF4WD68JR2Y";
    // 私有的访问密钥
    private static String sk = "nN1Y4Vi7TjlbABz1VMC4sdVywyKBH0dUlJZw223S";
    // 云服务所在的项目id
    private static String projectId = "05999653698026a82fc1c013159ed5a8";
    // 华为云账号id
    private static String domainId = "0589a289f880250a0f89c0138b27c800";
    //endpoint Url
    private static String endpoint = "https://ecs.cn-north-4.myhuaweicloud.com";
    // 实例id
    private static String serId = "b13618a8-ee74-44ba-8620-dc41b032e631";
    private static String serId2 = "a1d6ccc8-4b9a-468d-b75f-467d62501e9d";

    // 子网id
    private static String subNetId = "6b6dbad3-3877-43ea-b443-5f1b421c054a";
    // 子网地址
    private static String newIpAddr = "192.168.0.244";

    // 网卡ID
    private static String nicId = "a9dbaf4a-cd52-4021-8cfc-7de8000d7a39";

    // 配置客户端属性
    private static HttpConfig config = HttpConfig.getDefaultHttpConfig().withIgnoreSSLVerification(true);

    // 创建认证
    // Region级服务
    private static BasicCredentials auth = new BasicCredentials()
            .withAk(ak)
            .withSk(sk)
            .withProjectId(projectId);

    // Global级服务，全局服务当前仅支持IAM, TMS, EPS。
    private static GlobalCredentials credentials = new GlobalCredentials()
            .withAk(ak)
            .withSk(sk)
            .withDomainId(domainId);

    // 创建 ECS 客户端
    private static EcsClient ecsClient = EcsClient.newBuilder()
            .withHttpConfig(config)
            // 使用Region级服务认证
            .withCredential(auth)
            .withEndpoint(endpoint)
            .build();

    @Test
    public void listNic() {
        // 查询网卡信息
        listInterface(ecsClient, serId2);
    }

    @Test
    public void deleteNic() {
        // 批量删除网卡
        deleteInterface(ecsClient, serId2, nicId);
    }

    @Test
    public void addNic() {
        // 批量添加网卡
        addInterface(ecsClient, serId2, subNetId, newIpAddr);
    }

    /**
     * 查看服务器网卡信息
     * @param client
     * @param serId  服务器id
     */
    public static void listInterface(EcsClient client, String serId) {
        try {
            ListServerInterfacesResponse listServerInterfacesResponse =
                    client.listServerInterfaces(new ListServerInterfacesRequest().withServerId(serId));
            log.info(listServerInterfacesResponse.toString());
        } catch (ClientRequestException e) {
            log.error("HttpStatusCode: " + e.getHttpStatusCode());
            log.error("RequestId: " + e.getRequestId());
            log.error("ErrorCode: " + e.getErrorCode());
            log.error("ErrorMsg: " + e.getErrorMsg());
        }
    }

    /**
     * 查看用户配额信息
     *
     * @param ecsClient
     */
    public static void showLimits(EcsClient ecsClient) {
        try {
            ShowServerLimitsResponse showServerLimitsResponse = ecsClient.showServerLimits(new ShowServerLimitsRequest());
            log.info(showServerLimitsResponse.toString());
        } catch (ClientRequestException e) {
            log.error("HttpStatusCode: " + e.getHttpStatusCode());
            log.error("RequestId: " + e.getRequestId());
            log.error("ErrorCode: " + e.getErrorCode());
            log.error("ErrorMsg: " + e.getErrorMsg());
        }
    }

    /**
     * 查看服务器基本信息,
     *
     * @param ecsClient
     * @param serId     服务器id
     */
    public static void showServer(EcsClient ecsClient, String serId) {
        try {
            ShowServerResponse response = ecsClient.showServer(new ShowServerRequest().withServerId(serId));
            log.info(response.toString());
        } catch (ClientRequestException e) {
            log.error("HttpStatusCode: " + e.getHttpStatusCode());
            log.error("RequestId: " + e.getRequestId());
            log.error("ErrorCode: " + e.getErrorCode());
            log.error("ErrorMsg: " + e.getErrorMsg());
        }
    }

    /**
     * 添加一个网卡或者多个网卡
     *
     * @param ecsClient
     * @param serId
     * @param subNet
     * @param ipAddr
     */
    public static void addInterface(EcsClient ecsClient, String serId, String subNet, String ipAddr) {
        try {
            BatchAddServerNicOption option = new BatchAddServerNicOption()
                    .withSubnetId(subNet)
                    .withIpAddress(ipAddr);

            BatchAddServerNicsRequestBody body = new BatchAddServerNicsRequestBody()
                    .addNicsItem(option);

            BatchAddServerNicsRequest request = new BatchAddServerNicsRequest()
                    .withServerId(serId)
                    .withBody(body);

            BatchAddServerNicsResponse batchAddServerNicsResponse = ecsClient.batchAddServerNics(request);
            log.info(batchAddServerNicsResponse.toString());
        } catch (ClientRequestException e) {
            log.error("HttpStatusCode: " + e.getHttpStatusCode());
            log.error("RequestId: " + e.getRequestId());
            log.error("ErrorCode: " + e.getErrorCode());
            log.error("ErrorMsg: " + e.getErrorMsg());
        }
    }

    /**
     * 删除一个或者多个网卡
     *
     * @param ecsClient
     * @param serId
     * @param nicId
     */
    public static void deleteInterface(EcsClient ecsClient, String serId, String nicId) {
        try {
            List<BatchDeleteServerNicOption> option = new ArrayList<BatchDeleteServerNicOption>();
            option.add(new BatchDeleteServerNicOption().withId(nicId));
            BatchDeleteServerNicsRequestBody body = new BatchDeleteServerNicsRequestBody()
                    .withNics(option);
            BatchDeleteServerNicsRequest request = new BatchDeleteServerNicsRequest()
                    .withServerId(serId)
                    .withBody(body);
            BatchDeleteServerNicsResponse response = ecsClient.batchDeleteServerNics(request);
            log.info(response.toString());
        } catch (ClientRequestException e) {
            log.error("HttpStatusCode: " + e.getHttpStatusCode());
            log.error("RequestId: " + e.getRequestId());
            log.error("ErrorCode: " + e.getErrorCode());
            log.error("ErrorMsg: " + e.getErrorMsg());
        }
    }
}