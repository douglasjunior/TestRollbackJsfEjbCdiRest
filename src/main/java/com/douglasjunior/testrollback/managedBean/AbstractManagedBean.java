package com.douglasjunior.testrollback.managedBean;

import java.io.Serializable;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class AbstractManagedBean implements Serializable {

    public static final String FACES_REDIRECT = "faces-redirect=true";

    public Object getManagedBean(String managedBeanName) {
        FacesContext instance = FacesContext.getCurrentInstance();
        ExternalContext context = instance.getExternalContext();
        Map<String, Object> applicationMap = context.getApplicationMap();
        return applicationMap.get(managedBeanName);
    }

    private void sendMessage(String summary, String detail, FacesMessage.Severity severityError) {
        FacesContext instance = FacesContext.getCurrentInstance();
        instance.getExternalContext().getFlash().setKeepMessages(true);
        instance.addMessage(null, new FacesMessage(severityError, summary, detail));
    }

    public void sendErrorMessage(String message) {
        sendMessage(message, message, FacesMessage.SEVERITY_ERROR);
    }

    public void sendInfoMessage(String message) {
        sendMessage(message, message, FacesMessage.SEVERITY_INFO);
    }

    public String facesRedirect(String to) {
        if (to.contains("?")) {
            return to + "&" + FACES_REDIRECT;
        }
        return to + "?" + FACES_REDIRECT;
    }

}
