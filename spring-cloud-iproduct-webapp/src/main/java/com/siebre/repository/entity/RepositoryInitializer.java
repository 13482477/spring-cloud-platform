package com.siebre.repository.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.siebre.repository.GeneralRepository;
import com.siebre.repository.Repository;
import com.siebre.repository.rdb.hibernate.HibernateAware;
import com.siebre.repository.rdb.hibernate.HibernateUtils;
import com.siebre.repository.support.StaticGeneralRepository;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * Service used to initialize {@link Repository repositories} with EntityProviders.
 *
 * @author ZhangChi
 * @since 2012-12-18
 *
 */
public class RepositoryInitializer implements ApplicationContextAware, InitializingBean, ApplicationListener {

    protected Logger logger = LoggerFactory.getLogger(getClass());

	/* dependencies */

    private GeneralRepository repository;

    private PlatformTransactionManager txManager;

    private ApplicationContext appContext;

	/* parameters */

    private Class<?> entityBaseClass;

    private boolean autoInitialize = false;

    private boolean lenient = false;

    private boolean autoRegisterProvider = true;

    private List<EntityProvider<?>> providers = new ArrayList<EntityProvider<?>>();

    //TODO add EntityFilters

	/* internal states */

    private boolean initialized = false;

    private EntityProviderConsumer providerConsumer;

    private GeneralRepository proxyCapableRepository;

    private GeneralRepository repositoryUsedByProvider;

    private List<RepositoryInitializerListener> listeners = Lists.newArrayList();

    public void afterPropertiesSet() throws Exception {
        if (repository == null)//fail fast at startup time
            throw new IllegalStateException("repository can not be null");
    }

    private void setup() {
        if (autoRegisterProvider)
            registerProvidersInContext();

        providerConsumer = determineProviderConsumer();

        EntityDependencyGeneralRepository entityDependencyDecorator = new EntityDependencyGeneralRepository(repository);
        entityDependencyDecorator.getDescriptorFactory().setDefaultSuperClass(entityBaseClass);
        listeners.add(entityDependencyDecorator);

        proxyCapableRepository = new InitializingDependencyGeneralRepository(entityDependencyDecorator, this);
        repositoryUsedByProvider = new StaticGeneralRepository(proxyCapableRepository);
    }

    private void registerProvidersInContext() {
        if (appContext == null) {
            logger.debug("ApplicationContext is unavailable");
            return;
        }
        for (EntityProvider<?> scanned : appContext.getBeansOfType(EntityProvider.class).values())
            addProvider(scanned);
    }

    private EntityProviderConsumer determineProviderConsumer() {
        if (repository instanceof HibernateAware) {
            SessionFactory sessionFactory = ((HibernateAware) repository).getSessionFactory();
            return new HibernateEntityProviderConsumer(sessionFactory);
        }
        return new EntityProviderConsumer();
    }

    /**
     * Initialize all repositories with entities acquired from registered EntityProviders.
     */
    public void initialize() {
        if (repository == null)
            throw new IllegalStateException("repository can not be null");

        setup();

        logger.info("Underlying repository is: {}", repository);
        logger.info("There are {} provider(s):", providers.size());
        if (logger.isInfoEnabled()) {
            int index = 0;
            for (EntityProvider<?> provider : providers)
                logger.info("{}:{}", ++index, provider);
        }

        for (EntityProvider<?> provider : providers)
            consume(provider);

        for (RepositoryInitializerListener listener : listeners)
            listener.afterCompletion();

        initialized = true;
    }

    private Set<Class<?>> requestedTypes = Sets.newHashSet();

    public void initialize(Class<?> entityType) {
        if (requestedTypes.contains(entityType))
            return;

        for (EntityProvider<?> provider : findProviders(entityType))
            consume(provider);

        requestedTypes.add(entityType);
    }

    /** hold consumed EntityProviders to avoid duplicate consuming */
    private Set<EntityProvider<?>> consumedProviders = Sets.newHashSet();

    private void consume(EntityProvider<?> provider) {
        if (consumedProviders.contains(provider)) {
            logger.debug("Skip consumed provider: {}", provider);
            return;
        }

        provider.setGeneralRepository(repositoryUsedByProvider);

        try {
            providerConsumer.consume(provider);
        } catch (RuntimeException e) {
            if (lenient)
                logger.error("Failed to persist entities from provider: " + provider, e);
            else
                throw e;
        } finally {
            consumedProviders.add(provider);
        }
    }

    /**
     * Find all EntityProviders that can provide entity of specified type.
     * <p>
     * Sub type of specified type is also considered.
     *
     * @param entityType
     * @return
     */
    private Collection<EntityProvider<?>> findProviders(Class<?> entityType) {
        Collection<EntityProvider<?>> result = Lists.newArrayList();
        for (EntityProvider<?> provider : providers) {
            if (entityType.isAssignableFrom(provider.getEntityType()))
                result.add(provider);
        }
        return result;
    }

//	/**
//	 * Resolve the actual entity instance of EntityDescriptors and
//	 * apply invocations to it.
//	 *
//	 * @param eDescs
//	 */
//	private void resolveEntityDescriptors(Collection<EntityDescriptor> eDescs) {
//		logger.debug("resolving EntityDescriptors, total count: {}", eDescs.size());
//		for (EntityDescriptor ed : eDescs) {
//			Object entity = resolve(ed);
//			if (entity == null)
//				throw new RepositoryInitializationException("Failed to resolve entity descriptor " + ed);
//
//			repository.save(entity);
//		}
//	}

//	/**
//	 * Resolve EntityDescriptor to the actual entity instance.
//	 *
//	 * @param eDesc
//	 * @return null if the matched entity instance cannot be found.
//	 */
//	protected Object resolve(EntityDescriptor eDesc) {
//		Object result = resolveInternal(eDesc.getEntityType(), eDesc.getSelector());
//		if (eDesc.getEntity() == null)
//			eDesc.setEntity(result);
//		else if (!eDesc.getEntity().equals(result))
//			throw new EntityResolutionException("Different resolution result for EntityDescriptor: " + eDesc);
//
//		if (result != null)
//			applyInvocations(eDesc);
//		return result;
//	}

//	private <T> T resolveInternal(Class<T> entityType, EntitySelector selector) {
//		if (repository == null)
//			return null;
//
//		T result = null;
//		if (selector instanceof KeyValueSelector) {
//			KeyValueSelector typed = (KeyValueSelector) selector;
//			result = repository.findOne(entityType, typed.getConditions());
//		} else if (selector instanceof IndexSelector) {
//			IndexSelector typed = (IndexSelector) selector;
//			result = Iterables.get(repository.findAll(entityType), typed.index);
//		}
//
//		return result;
//	}

//	private void applyInvocations(EntityDescriptor eDesc) {
//		Object entity = eDesc.getEntity();
//		for (Invocation invocation : eDesc.getInvocations()) {
//			try {
//				invocation.apply(entity);
//			} catch (Exception e) {
//				throw new EntityResolutionException("Failed to apply invocation to " + entity, e);
//			}
//		}
//	}

//	private void resolveEntityReferences(Set<EntityReference> entityReferences) {
//		logger.debug("resolving EntityReferences, total count: {}", entityReferences.size());
//		for (EntityReference eRef : entityReferences) {
//			if (eRef.getOrigin() == null)
//				throw new RepositoryInitializationException("Origin of entity reference " + eRef + " is missing");
//
//			buildReference(eRef);
//
//			//TODO optimization issue: one entity may be saved multiple times.
//			//make sure each entity is saved only after all its entity references has been resolved.
//			repository.save(eRef.getOrigin());//transaction assumed
//		}
//	}

    public void addProvider(EntityProvider<?> entityProvider) {
        if (providers.contains(entityProvider)) {
            logger.warn("Ignore duplicate entity provider: {}", entityProvider);
            return;
        }
        providers.add(entityProvider);
    }

    public void setTxManager(PlatformTransactionManager txManager) {
        this.txManager = txManager;
    }

    public void setRepository(GeneralRepository repository) {
        this.repository = repository;
    }

    public void setEntityBaseClass(Class<?> entityBaseClass) {
        this.entityBaseClass = entityBaseClass;
    }

    public void setAutoInitialize(boolean autoInitialize) {
        this.autoInitialize = autoInitialize;
    }

    public void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    public void setAutoRegisterProvider(boolean autoRegisterProvider) {
        this.autoRegisterProvider = autoRegisterProvider;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }

    public void onApplicationEvent(ApplicationEvent event) {
        if (initialized || !autoInitialize)
            return;
        if (!(event instanceof ContextRefreshedEvent))
            return;

        synchronized(this) {
            //initialize();
        }
    }

    class EntityProviderConsumer {

        void consume(EntityProvider<?> provider) {
            List<?> entities = provider.provide();
            logger.info("{} entities retrieved from provider {}", entities.size(), provider);
            for (Object entity : entities) {
                proxyCapableRepository.save(entity);//transaction assumed
            }
        }
    }

    class HibernateEntityProviderConsumer extends EntityProviderConsumer {

        private TransactionTemplate txTemplate;

        private SessionFactory sessionFactory;

        public HibernateEntityProviderConsumer(SessionFactory sessionFactory) {
            if (sessionFactory == null)
                throw new NullPointerException("sessionFactory cannot be null");
            if (txManager == null)
                throw new NullPointerException("platformTransactionManager cannot be null");
            txTemplate = new TransactionTemplate(txManager);
            this.sessionFactory = sessionFactory;
        }

        public void consume(final EntityProvider<?> provider) {
            txTemplate.execute(new TransactionCallbackWithoutResult() {

                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    logger.debug("set current session flush mode to COMMIT");
                    Session session = HibernateUtils.getCurrentSession(sessionFactory);
                    session.setFlushMode(FlushMode.COMMIT);//avoid the flush action triggered by reading
                    //XXX potential issue: the flush mode should be reset if there is a wrapping transaction.
                    HibernateEntityProviderConsumer.super.consume(provider);//XXX potential issue: big transaction
                }
            });
        }
    }

    /**
     *
     * @author ZhangChi
     * @since 2013-1-26
     */
    static interface RepositoryInitializerListener {

        void afterCompletion();
    }
}
