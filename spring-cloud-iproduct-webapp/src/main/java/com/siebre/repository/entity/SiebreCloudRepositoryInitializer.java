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
public class SiebreCloudRepositoryInitializer extends RepositoryInitializer {

    @Override
    public void initialize() {
        //super.initialize();
        //do nothing
    }

}
