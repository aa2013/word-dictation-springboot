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
import org.jsoup.select.Elements;

import java.util.*;

public class SouGouTranslator extends AbstractTranslator<JSONObject> {
    private boolean notFound = false;

    @Override
    protected boolean isNotFound() {
        return this.notFound;
    }

    @Override
    public void translate(String word) {
        try {
            String resp = NetUtil.get("https://fanyi.sogou.com/text?keyword=" + word);
            Document document = Jsoup.parse(resp);
            Elements notFind = document.getElementsByClass("word-details-card");
            this.notFound = notFind.size() == 0;
            Element translateIndex = document.getElementById("translateIndex");
            if (translateIndex == null) {
                this.notFound = true;
                return;
            }
            Element script = translateIndex.nextElementSibling();
            if (script == null) {
                this.notFound = true;
                return;
            }
            String html = script.html();
            String[] replace = new String[]{
                    "window.__INITIAL_STATE__=",
                    ";(function(){var s;(s=document.currentScript||document.scripts[document.scripts.length-1]).parentNode.removeChild(s);}());"
            };
            for (String rep : replace) {
                html = html.replace(rep, "");
            }
            this.data = JSONObject.parseObject(html);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnavailableException("解析Html错误");
        }
    }

    @Override
    public String[] getDetailSymbols() {
        String[] symbols = new String[]{null, null};
        JSONArray list = this.data.getJSONObject("voice")
                .getJSONObject("from")
                .getJSONArray("list");
        int size = list.size();
        JSONObject[] phonetics = {
                size > 0 ? list.getJSONObject(0) : null,
                size > 1 ? list.getJSONObject(1) : null
        };
        for (JSONObject phonetic : phonetics) {
            if (phonetic == null) {
                return symbols;
            }
            String type = phonetic.getString("type");
            if (type.contains("uk")) {
                symbols[0] = phonetic.getString("phonetic");
            } else if (type.contains("us")) {
                symbols[1] = phonetic.getString("phonetic");
            }
        }
        return symbols;
    }

    @Override
    public String[] getDetailSymbolMp3s() {
        String[] mp3s = new String[]{null, null};
        final String mp3Preview = "https:";
        JSONObject form = this.data.getJSONObject("voice")
                .getJSONObject("form");
        if (form == null) {
            return mp3s;
        }
        JSONArray voices = form.getJSONArray("list");
        int size = voices.size();
        JSONObject[] mp3Objs = {
                size > 0 ? voices.getJSONObject(0) : null,
                size > 1 ? voices.getJSONObject(1) : null
        };
        for (JSONObject mp3 : mp3Objs) {
            if (mp3 == null) {
                return mp3s;
            }
            String type = mp3.getString("type");
            if (type.contains("uk")) {
                mp3s[0] = mp3Preview + mp3.getString("src");
            } else if (type.contains("us")) {
                mp3s[1] = mp3Preview + mp3.getString("src");
            }
        }
        return mp3s;
    }

    @Override
    public List<Explain> getDetailExplains() {
        String keys = "textTranslate,translateData,wordCard";
        JSONObject data = this.data;
        for (String key : keys.split(",")) {
            data = data.getJSONObject(key);
        }
        JSONArray usualDict = data.getJSONArray("usualDict");
        List<Explain> explains = new LinkedList<>();
        for (int i = 0; i < usualDict.size(); i++) {
            JSONObject dict = usualDict.getJSONObject(i);
            String type = dict.getString("pos");
            JSONArray values = dict.getJSONArray("values");
            for (int j = 0; j < values.size(); j++) {
                String[] means = values.getString(j).split("；");
                List<Explain> temp = new ArrayList<>(means.length);
                for (String mean : means) {
                    temp.add(new Explain(mean, type));
                }
                explains.addAll(temp);
            }
        }
        if (usualDict.size() == 0) {
            throw new UnavailableException("翻译结果为空");
        }
        return explains;
    }
}
