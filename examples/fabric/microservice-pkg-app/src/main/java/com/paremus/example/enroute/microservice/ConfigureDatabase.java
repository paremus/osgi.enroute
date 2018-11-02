package com.paremus.example.enroute.microservice;

import org.bndtools.service.endpoint.Endpoint;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jdbc.DataSourceFactory;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

@Component
public class ConfigureDatabase {
    final static String txcPid = "org.apache.aries.tx.control.jdbc.xa";

    @Reference
    ConfigurationAdmin cfgadm;

    private String uri;
    private String driverClass;

    @Reference(target = "(&(uri=jdbc:*)(path=enroute)(driverClass=*))")
    void setEndpoint(Endpoint endpoint, Map<String, Object> svcProps) {
        driverClass = (String) svcProps.get("driverClass");
        Object xuri = svcProps.get("uri");

        if (xuri.getClass().isArray()) {
            Object[] uris = (Object[]) xuri;
            uri = uris[0].toString();
        } else {
            uri = xuri.toString();
        }
    }

    @Activate
    void activate(Map<String, Object> config) throws IOException {
        Dictionary<String, Object> props = new Hashtable<>();
        props.put("name", "microservice.database");
        props.put(DataSourceFactory.OSGI_JDBC_DRIVER_CLASS, driverClass);
        props.put(DataSourceFactory.JDBC_URL, uri);

        String user = (String) config.get("user");
        String pw = (String) config.get(".password");

        if (user != null) {
            props.put(DataSourceFactory.JDBC_USER, user);
        }

        if (pw != null) {
            props.put(DataSourceFactory.JDBC_PASSWORD, pw);
        }

        Configuration txcConfig = cfgadm.createFactoryConfiguration(txcPid, "?");
        txcConfig.update(props);
    }

}
