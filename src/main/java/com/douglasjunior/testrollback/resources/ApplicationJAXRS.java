package com.douglasjunior.testrollback.resources;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.ApplicationPath;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.jettison.JettisonFeature;

@ApplicationPath("/resources/*")
public class ApplicationJAXRS extends Application {

    public ApplicationJAXRS() {
        
    }
    
    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("jersey.config.server.provider.packages", "com.douglasjunior.testrollback.resources");
        return properties;
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = new HashSet<>();
        singletons.add(new JettisonFeature());

        return singletons;
    }

}
