package br.com.bancoamazonia.infrend.job.core.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Encapsula o contexto do projeto infrend
 * @author rudolph
 *
 */
@Scope("singleton")
@Component
public class InfrendContext implements ApplicationContextAware {
	
	private static ApplicationContext context;
	
	static {
	/**
	 * instancia ApplicationContext programaticamente
	 */
		context = new ClassPathXmlApplicationContext(new String[] {"modelContext.xml", "coreContext.xml", "serviceContext.xml"});
	}

	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		initializeApplicationContext(ctx);
	}
	
	private static void initializeApplicationContext(ApplicationContext applicationContext) {
	   context = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext() {
		return context;
	}
	
	public static Object getBean(String bean) {
		return context.getBean(bean);
	}
	
	public static <T> T getBean(Class<T> bean) {
		return context.getBean(bean);
	}

}
