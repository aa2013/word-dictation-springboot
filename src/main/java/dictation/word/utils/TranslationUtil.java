package dictation.word.utils;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class TranslationUtil {

    private static final String JINSAN = "https://www.iciba.com/word?w=";
    private static final String SOUGOU = "https://fanyi.sogou.com/text?keyword=";

    public static JSONObject getCiBa(String word) throws IOException {
        String resp = NetUtil.get(JINSAN + word);
        Document parse = Jsoup.parse(resp);
        Element data = parse.getElementById("__NEXT_DATA__");
        assert data != null;
        JSONObject json = JSONObject.parseObject(data.html());
        String[] keys = new String[]{"props", "pageProps", "initialReduxState", "word", "wordInfo", "baesInfo"};
        for (String key : keys) {
            json = json.getJSONObject(key);
        }
        return json.getJSONArray("symbols").getJSONObject(0);
    }
}
