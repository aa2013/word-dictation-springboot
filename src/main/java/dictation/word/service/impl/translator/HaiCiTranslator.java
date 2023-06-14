package dictation.word.service.impl.translator;

import dictation.word.entity.word.Explain;
import dictation.word.exception.UnavailableException;
import dictation.word.service.i.word.AbstractTranslator;
import dictation.word.utils.NetUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HaiCiTranslator extends AbstractTranslator<Document> {
    @Override
    protected boolean isNotFound() {
        Elements notFind = this.data.getElementsByClass("ifufind");
        return notFind.size() > 0;
    }

    @Override
    public void translate(String word) {
        try {
            String resp = NetUtil.get("https://dict.cn/search?q=" + word);
            this.data = Jsoup.parse(resp);
        } catch (Exception e) {

            e.printStackTrace();
            throw new UnavailableException("解析Html错误");
        }
    }

    @Override
    public String[] getDetailSymbols() {
        String[] symbols = new String[]{null, null};
        Elements elements = this.data.select(".phonetic bdo");
        int size = elements.size();
        Element enEle = size > 0 ? elements.get(0) : null;
        Element usEle = size > 1 ? elements.get(1) : null;
        if (enEle != null) {
            symbols[0] = enEle.text()
                    .replace("[", "")
                    .replace("]", "");
        }
        if (usEle != null) {
            symbols[1] = usEle.text()
                    .replace("[", "")
                    .replace("]", "");
        }
        return symbols;
    }

    @Override
    public String[] getDetailSymbolMp3s() {
        String[] mp3s = new String[]{null, null};
        Elements elements = this.data.select(".phonetic .sound.fsound");
        int size = elements.size();
        Element enMp3 = size > 0 ? elements.get(0) : null;
        Element usMp3 = size > 1 ? elements.get(1) : null;
        final String mp3Host = "https://audio.dict.cn/";
        if (enMp3 != null) {
            mp3s[0] = mp3Host + enMp3.attr("naudio");
        }
        if (usMp3 != null) {
            mp3s[1] = mp3Host + usMp3.attr("naudio");
        }
        return mp3s;
    }

    @Override
    public List<Explain> getDetailExplains() {
        Elements lis = this.data.select(".basic.clearfix li");
        List<Element> subLis = lis.subList(0, lis.size() - 1);
        List<Explain> res = new LinkedList<>();
        subLis.forEach(li -> {
            String type = li.child(0).text();
            String[] means = li.child(1).text().split("；");
            List<Explain> temp = new ArrayList<>(means.length);
            for (String mean : means) {
                temp.add(new Explain(mean, type));
            }
            res.addAll(temp);
        });
        return res;
    }
}
