package dictation.word.entity.word;

import dictation.word.exception.UnavailableException;
import dictation.word.service.i.word.AbstractTranslator;
import dictation.word.service.impl.translator.CiBaTranslator;
import dictation.word.service.impl.translator.HaiCiTranslator;
import dictation.word.service.impl.translator.SouGouTranslator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class TranslationResult {
    /**
     * AbstractTranslator类抽象出翻译接口，具体由子类实现
     * translators 用于保存所有可用翻译器
     */
    private static final List<AbstractTranslator<?>> translators = new ArrayList<>();

    //配置你的翻译接口，配置顺序影响调用顺序
    static {
        translators.add(new HaiCiTranslator());
        translators.add(new CiBaTranslator());
        translators.add(new SouGouTranslator());
    }

    private String enSymbol;
    private String enMp3;
    private String usSymbol;
    private String usMp3;
    private List<Explain> explains;

    public static TranslationResult translate(String word) {
        TranslationResult result = null;
        //遍历所有翻译器，以使得其中一个翻译器出错时可以调用另一个翻译器完成翻译工作，当翻译成功后直接结束
        for (AbstractTranslator<?> translator : translators) {
            Class<?> clazz = translator.getClass();
            try {
                //开始词典翻译
                translator.translate(word);
                //调用对应实现类获取并整合翻译结果
                TranslationResult temp = new TranslationResult();
                //获取音标
                String[] symbols = translator.getSymbols();
                //获取音标发音链接
                String[] symbolMp3s = translator.getSymbolMp3s();
                //获取释义内容
                List<Explain> explainList = translator.getExplains();
                //封装结果
                temp.setExplains(explainList);
                temp.setEnSymbol(symbols[0]);
                temp.setUsSymbol(symbols[1]);
                temp.setEnMp3(symbolMp3s[0]);
                temp.setUsMp3(symbolMp3s[1]);
                //清除当前翻译信息
                translator.clear();
                result = temp;
                log.info("use translator: {}({}). successfully", clazz.getSimpleName(), clazz.getName());
                break;
            } catch (Exception e) {
                log.error("use translator: {}({}). error", clazz.getSimpleName(), clazz.getName());
                e.printStackTrace();
            }
        }
        if (translators.isEmpty()) {
            throw new UnavailableException("未设置翻译器");
        }
        if (result == null) {
            throw new UnavailableException("查询失败，单词不存在或被翻译API禁止");
        }
        return result;
    }
}
