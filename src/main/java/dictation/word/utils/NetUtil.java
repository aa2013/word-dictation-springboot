package dictation.word.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class NetUtil {

    public static String get(String getUrl) throws IOException {
        getUrl = getUrl.replace(" ", "%20");
        // 创建一个可关闭的 HttpClient 实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建一个 HttpGet 请求
        HttpGet httpGet = new HttpGet(getUrl);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
        // 发送请求并获取响应
        CloseableHttpResponse response = httpClient.execute(httpGet);
        // 读取响应内容
        String content = EntityUtils.toString(response.getEntity());
        // 打印响应状态码和状态信息
        if (response.getStatusLine().getStatusCode() != 200) {
            System.out.println(content);
            throw new RuntimeException("获取响应失败");
        }
        // 关闭响应
        response.close();
        // 关闭 HttpClient
        httpClient.close();
        return content;
    }
}
