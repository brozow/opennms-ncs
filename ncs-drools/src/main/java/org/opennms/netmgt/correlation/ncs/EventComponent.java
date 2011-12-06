package org.opennms.netmgt.correlation.ncs;

import org.opennms.netmgt.model.ncs.NCSComponent;
import org.opennms.netmgt.xml.event.Event;

public class EventComponent {
    
    NCSComponent component;
    Event        event;
    
    
    public EventComponent(NCSComponent component, Event event) {
        super();
        this.component = component;
        this.event = event;
    }
    
    public NCSComponent getComponent() {
        return component;
    }
    
    public void setComponent(NCSComponent component) {
        this.component = component;
    }
    
    public Event getEvent() {
        return event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }
    
    
    

}
