package org.opennms.netmgt.stats;

import org.springframework.beans.factory.InitializingBean;

public abstract class AbstractBaseStatisticsService<T> implements StatisticsService<T>, InitializingBean {

    @Override
    public void afterPropertiesSet() {
    }
}
