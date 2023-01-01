package dictation.word.service.impl.word;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dictation.word.dao.word.WordExplainMapper;
import dictation.word.entity.word.Explain;
import dictation.word.entity.word.tables.WordExplain;
import dictation.word.service.i.user.TokenService;
import dictation.word.service.i.word.WordExplainService;
import dictation.word.utils.NetUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static dictation.word.entity.word.tables.Word.JINSAN;

/**
 * @author ljh
 * @date 2022/12/14
 */
@Service
public class WordExplainServiceImpl extends ServiceImpl<WordExplainMapper, WordExplain> implements WordExplainService, TokenService {


    @Override
    public List<Explain> getExplains(String word) throws IOException {
        String resp = NetUtil.get(JINSAN + word);
        Document document = Jsoup.parse(resp);
        Elements explainsUl = document.getElementsByClass("Mean_part__UI9M6");
        return parseExplain(explainsUl);
    }


    public List<Explain> parseExplain(Elements es) {
        List<Explain> res = new ArrayList<>();
        Elements children = es.get(0).children();
        for (Element element : children) {
            String type = element.child(0).text();
            Elements explains = element.child(1).children();
            List<Explain> explainList = new ArrayList<>();
            for (Element explain : explains) {
                explainList.add(new Explain(explain.text().replace(";", ""), type));
            }
            res.addAll(explainList);
        }
        return res;
    }
}
