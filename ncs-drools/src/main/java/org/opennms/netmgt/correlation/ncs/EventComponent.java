package org.opennms.netmgt.correlation.ncs;


import org.opennms.netmgt.xml.event.Event;

public class EventComponent {
    
    Component component;
    Event        event;
    
    
    public EventComponent(Component component, Event event) {
        super();
        this.component = component;
        this.event = event;
    }
    
    public Component getComponent() {
        return component;
    }
    
    public void setComponent(Component component) {
        this.component = component;
    }
    
    public Event getEvent() {
        return event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }
    
    
    

}
