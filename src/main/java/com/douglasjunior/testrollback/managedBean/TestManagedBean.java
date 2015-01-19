package com.douglasjunior.testrollback.managedBean;

import com.douglasjunior.testrollback.dao.GenericDao;
import com.douglasjunior.testrollback.model.User;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Named(value = "testManagedBean")
@ViewScoped
public class TestManagedBean extends AbstractManagedBean {

    @Inject
    private GenericDao dao;
    
    private User user;

    public TestManagedBean() {
        user = new User();
    }

    @Transactional(Transactional.TxType.REQUIRED) // The solution
    public String test() {
        if (user.getSomeText() == null || user.getSomeText().isEmpty()) {
            sendErrorMessage("Text is riquired!");
            return null;
        }
        dao.persist(user);
        User user2 = new User();
        user2.setSomeText(user.getSomeText()); // "someText" is unique on database
        dao.persist(user2); // org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint "table_user_sometext_key"
        return null;
    }

    public User getUser() {
        return user;
    }    
}
