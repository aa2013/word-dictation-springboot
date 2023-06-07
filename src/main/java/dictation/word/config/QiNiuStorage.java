package dictation.word.config;

import com.alibaba.fastjson.JSON;
import com.qiniu.cdn.CdnManager;
import com.qiniu.cdn.CdnResult;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QiNiuStorage {
    @Value("${qiniu.accessKey:}")
    private String accessKey;
    @Value("${qiniu.secretKey:}")
    private String secretKey;
    @Value("${qiniu.bucketName:}")
    private String bucketName;
    @Value("${qiniu.domain:}")
    private String domain;

    private String getCDNUrl(String filePath) {
        return "http://" + domain + "/" + filePath;
    }

    public String upload(String filePath, byte[] bytes) {
        //去除开头的所有斜杠
        filePath = filePath.replaceAll("^/+", "");
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration();
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;
        String bucket = this.bucketName;
        Auth auth = Auth.create(accessKey, secretKey);
        //传入第二个参数filePath则表示覆盖上传
        String upToken = auth.uploadToken(bucket, filePath);
        try {
            Response response = uploadManager.put(bytes, filePath, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            String hash = putRet.hash;
            return getCDNUrl(putRet.key);
        } catch (QiniuException ex) {
            ex.printStackTrace();
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                ex2.printStackTrace();
            }
        }
        return null;
    }

    public String uploadByBase64(String filePath, String base64) {
        base64 = base64.substring(base64.indexOf(",") + 1);
        byte[] bytes = Base64.decodeBase64(base64);
        return upload(filePath, bytes);
    }

    public void refresh(String url) {
        Auth auth = Auth.create(accessKey, secretKey);
        CdnManager c = new CdnManager(auth);
        //待刷新的链接列表
        String[] urls = new String[]{url,};
        try {
            //单次方法调用刷新的链接不可以超过100个
            CdnResult.RefreshResult result = c.refreshUrls(urls);
            System.out.println(result.code);
            //获取其他的回复内容
        } catch (QiniuException e) {
            System.err.println(e.response.toString());
        }
    }
}
