package com.douglasjunior.testrollback.resources;

import com.douglasjunior.testrollback.dao.GenericDao;
import com.douglasjunior.testrollback.model.User;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("test")
@Stateless
public class TestResource {

    @Inject
    private GenericDao dao;

    @Context
    private UriInfo context;
    
    public TestResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getTeste(@QueryParam("someText") String someText) {
        if (someText == null || someText.isEmpty()) {
            throw new WebApplicationException("Text is riquired!", Response.Status.BAD_REQUEST);
        }
        System.out.println("someText: " + someText);
        User user = new User();
        user.setSomeText(someText);
        dao.persist(user);
        User user2 = new User();
        user2.setSomeText(user.getSomeText()); // "someText" is unique on database
        dao.persist(user2); // org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint "table_user_sometext_key"
        return user;
    }

}
