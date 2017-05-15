package com.siebre.basic.db;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;

/**
 * 
 * @author 李志强
 *
 */
public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner {
	private boolean addToConfig = true;
	private SqlSessionFactory sqlSessionFactory;
	private DynamicSqlSessionTemplate sqlSessionTemplate;
	private String sqlSessionTemplateBeanName;
	private String sqlSessionFactoryBeanName;
	private Class<? extends Annotation> annotationClass;
	private Class<?> markerInterface;
	private MapperFactoryBean mapperFactoryBean = new MapperFactoryBean();

	public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
		super(registry, false);
	}

	public void setAddToConfig(boolean addToConfig) {
		this.addToConfig = addToConfig;
	}

	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	public void setMarkerInterface(Class<?> markerInterface) {
		this.markerInterface = markerInterface;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public void setSqlSessionTemplate(DynamicSqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public void setSqlSessionTemplateBeanName(String sqlSessionTemplateBeanName) {
		this.sqlSessionTemplateBeanName = sqlSessionTemplateBeanName;
	}

	public void setSqlSessionFactoryBeanName(String sqlSessionFactoryBeanName) {
		this.sqlSessionFactoryBeanName = sqlSessionFactoryBeanName;
	}

	public void setMapperFactoryBean(MapperFactoryBean mapperFactoryBean) {
		this.mapperFactoryBean = (mapperFactoryBean != null ? mapperFactoryBean : new MapperFactoryBean());
	}

	public void registerFilters() {
		boolean acceptAllInterfaces = true;

		if (this.annotationClass != null) {
			addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
			acceptAllInterfaces = false;
		}

		if (this.markerInterface != null) {
			addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
				protected boolean matchClassName(String className) {
					return false;
				}
			});
			acceptAllInterfaces = false;
		}

		if (acceptAllInterfaces) {
			addIncludeFilter(new TypeFilter() {
				public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
					return true;
				}

			});
		}

		addExcludeFilter(new TypeFilter() {
			public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
				String className = metadataReader.getClassMetadata().getClassName();
				return className.endsWith("package-info");
			}
		});
	}

	public Set<BeanDefinitionHolder> doScan(String[] basePackages) {
		Set beanDefinitions = super.doScan(basePackages);

		if (beanDefinitions.isEmpty())
			this.logger.warn("No MyBatis mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
		else {
			processBeanDefinitions(beanDefinitions);
		}

		return beanDefinitions;
	}

	private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
		for (BeanDefinitionHolder holder : beanDefinitions) {
			GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();

			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '" + definition.getBeanClassName() + "' mapperInterface");
			}

			definition.getPropertyValues().add("mapperInterface", definition.getBeanClassName());
			definition.setBeanClass(this.mapperFactoryBean.getClass());

			definition.getPropertyValues().add("addToConfig", Boolean.valueOf(this.addToConfig));

			boolean explicitFactoryUsed = false;
			if (StringUtils.hasText(this.sqlSessionFactoryBeanName)) {
				definition.getPropertyValues().add("sqlSessionFactory", new RuntimeBeanReference(this.sqlSessionFactoryBeanName));
				explicitFactoryUsed = true;
			} else if (this.sqlSessionFactory != null) {
				definition.getPropertyValues().add("sqlSessionFactory", this.sqlSessionFactory);
				explicitFactoryUsed = true;
			}

			if (StringUtils.hasText(this.sqlSessionTemplateBeanName)) {
				if (explicitFactoryUsed) {
					this.logger.warn("Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.");
				}
				definition.getPropertyValues().add("sqlSessionTemplate", new RuntimeBeanReference(this.sqlSessionTemplateBeanName));
				explicitFactoryUsed = true;
			} else if (this.sqlSessionTemplate != null) {
				if (explicitFactoryUsed) {
					this.logger.warn("Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.");
				}
				definition.getPropertyValues().add("sqlSessionTemplate", this.sqlSessionTemplate);
				explicitFactoryUsed = true;
			}

			if (!explicitFactoryUsed) {
				if (this.logger.isDebugEnabled()) {
					this.logger.debug("Enabling autowire by type for MapperFactoryBean with name '" + holder.getBeanName() + "'.");
				}
				definition.setAutowireMode(2);
			}
		}
	}

	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return (beanDefinition.getMetadata().isInterface()) && (beanDefinition.getMetadata().isIndependent());
	}

	protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
		if (super.checkCandidate(beanName, beanDefinition)) {
			return true;
		}
		this.logger.warn("Skipping MapperFactoryBean with name '" + beanName + "' and '" + beanDefinition.getBeanClassName() + "' mapperInterface" + ". Bean already defined with the same name!");

		return false;
	}
}