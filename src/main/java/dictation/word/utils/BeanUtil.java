package dictation.word.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * bean工具类
 * @author ljh
 */

@Component
public class BeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    /**
     * 获取bean
     * @param name bean名称
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanUtil.applicationContext = applicationContext;
    }


}
