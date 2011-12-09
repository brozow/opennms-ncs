package org.opennms.netmgt.ncs.northbounder;

import java.util.List;

import org.opennms.netmgt.alarmd.api.Alarm;
import org.opennms.netmgt.alarmd.api.NorthbounderException;
import org.opennms.netmgt.alarmd.api.support.AbstractNorthbounder;


public class NCSNorthbounder extends AbstractNorthbounder {

	protected NCSNorthbounder() {
		super("NCS-Space-Northbounder");
	}

	@Override
	protected boolean accepts(Alarm alarm) {
		return true;
	}

	@Override
	public void forwardAlarms(List<Alarm> alarms) throws NorthbounderException {
		System.err.println(String.format("NCSNorthbounder received batch of %d alarms.", alarms.size()));
		for(Alarm alarm : alarms) {
			System.err.println(String.format("\tAlarm: %s", alarm.getUei()));
		}

	}


}
