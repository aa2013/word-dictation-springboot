package dictation.word.service.i.word;

import dictation.word.entity.word.Explain;
import dictation.word.exception.IllegalDataException;

import java.util.List;

public abstract class AbstractTranslator<T> {
    /**
     * 存储解析得到的源数据，有可能是 json，有可能是 html 的 document
     */
    protected T data = null;

    private void checkValid() {
        if (this.data == null || this.isNotFound()) {
            throw new IllegalDataException("未获取翻译结果或获取失败");
        }
    }

    /**
     * 标记是否无翻译结果
     */
    protected abstract boolean isNotFound();

    public void clear() {
        this.data = null;
    }

    public abstract void translate(String word);

    /**
     * 获取音标信息
     *
     * @return [0]：en [1]: us
     */
    public String[] getSymbols() {
        checkValid();
        return getDetailSymbols();
    }

    /**
     * 获取音标信息
     *
     * @return [0]：en [1]: us
     */
    protected abstract String[] getDetailSymbols();

    /**
     * 获取音标发音链接
     *
     * @return [0]：en [1]: us
     */
    public String[] getSymbolMp3s() {
        checkValid();
        return getDetailSymbolMp3s();
    }

    /**
     * 获取音标发音链接
     *
     * @return [0]：en [1]: us
     */
    protected abstract String[] getDetailSymbolMp3s();

    /**
     * 获取单词释义信息
     *
     * @return Explain 单词释义
     */
    public List<Explain> getExplains() {
        checkValid();
        return getDetailExplains();
    }

    /**
     * 获取单词释义信息
     *
     * @return Explain 单词释义
     */
    protected abstract List<Explain> getDetailExplains();
}
