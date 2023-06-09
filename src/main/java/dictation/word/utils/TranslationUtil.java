package dictation.word.utils;

import dictation.word.service.impl.translator.HaiCiTranslator;

import java.io.IOException;

public class TranslationUtil {

    private static final String JINSAN = "https://www.iciba.com/word?w=";
    private static final String SOUGOU = "https://fanyi.sogou.com/text?keyword=";
    private static final String HAICI = "https://dict.cn/search?q=";

    public static void main(String[] args) throws IOException {
        HaiCiTranslator translator = new HaiCiTranslator();
        translator.translate("go on");
        System.out.println(translator.getExplains());
    }
}
