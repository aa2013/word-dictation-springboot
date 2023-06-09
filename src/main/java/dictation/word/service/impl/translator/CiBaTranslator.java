package dictation.word.service.impl.translator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dictation.word.entity.word.Explain;
import dictation.word.exception.UnavailableException;
import dictation.word.service.i.word.AbstractTranslator;
import dictation.word.utils.NetUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CiBaTranslator extends AbstractTranslator<JSONObject> {
    @Override
    public void translate(String word) {
        try {
            String resp = NetUtil.get("https://www.iciba.com/word?w=" + word);
            Document document = Jsoup.parse(resp);
            Element element = document.getElementById("__NEXT_DATA__");
            assert element != null;
            JSONObject json = JSONObject.parseObject(element.html());
            String keys = "props,pageProps,initialReduxState,word,wordInfo,baesInfo";
            for (String key : keys.split(",")) {
                json = json.getJSONObject(key);
            }
            this.data = json.getJSONArray("symbols").getJSONObject(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnavailableException("解析Html错误");
        }
    }

    @Override
    public String[] getDetailSymbols() {
        String[] symbols = new String[]{null, null};
        symbols[0] = this.data.getString("ph_en");
        symbols[1] = this.data.getString("ph_am");
        return symbols;
    }

    @Override
    public String[] getDetailSymbolMp3s() {
        String[] symbolMp3s = new String[]{null, null};
        symbolMp3s[0] = this.data.getString("ph_en_mp3");
        symbolMp3s[1] = this.data.getString("ph_am_mp3");
        return symbolMp3s;
    }

    @Override
    public List<Explain> getDetailExplains() {
        List<Explain> explains = new ArrayList<>();
        JSONArray parts = data.getJSONArray("parts");
        for (int i = 0; i < parts.size(); i++) {
            JSONObject part = parts.getJSONObject(i);
            String type = part.getString("part");
            JSONArray means = part.getJSONArray("means");
            for (int j = 0; j < means.size(); j++) {
                explains.add(new Explain(means.getString(j), type));
            }
        }
        // 以短释义优先
        explains.sort(Comparator.comparingInt(a -> a.getExplanation().length()));
        return explains;
    }
}
