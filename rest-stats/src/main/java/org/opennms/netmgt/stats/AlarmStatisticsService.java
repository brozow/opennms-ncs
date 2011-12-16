package org.opennms.netmgt.stats;

import org.hibernate.criterion.Restrictions;
import org.opennms.netmgt.dao.AlarmDao;
import org.opennms.netmgt.model.OnmsAlarm;
import org.opennms.netmgt.model.OnmsCriteria;
import org.springframework.beans.factory.annotation.Autowired;

public class AlarmStatisticsService extends AbstractBaseStatisticsService<OnmsAlarm> {

    @Autowired
    private AlarmDao m_alarmDao;
    
    public int getTotalCount(final OnmsCriteria criteria) {
        return m_alarmDao.countMatching(criteria);
    }

    public int getAcknowledgedCount(final OnmsCriteria criteria) {
        criteria.add(Restrictions.isNotNull("ackUser"));
        return m_alarmDao.countMatching(criteria);
    }

}
