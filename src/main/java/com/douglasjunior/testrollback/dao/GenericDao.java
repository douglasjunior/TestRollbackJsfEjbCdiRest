package com.douglasjunior.testrollback.dao;

import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.math.NumberUtils;

@Stateless
public class GenericDao implements Serializable {

    @PersistenceContext(unitName = "PU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public void persist(Object entity) {
        getEntityManager().persist(entity);
        getEntityManager().flush();
    }

    public void edit(Object entity) {
        getEntityManager().merge(entity);
        getEntityManager().flush();
    }

    public void remove(Object entity) {
        getEntityManager().remove(entity);
        getEntityManager().flush();
    }

    public <T> T findByID(Long id, Class<T> classe) {
        return getEntityManager().find(classe, id);
    }

    public <T> T findByID(String strId, Class<T> classe) {
        Long lId = NumberUtils.toLong(strId);
        return findByID(lId, classe);
    }

    public <T> T findByID(String strId, String strClasse) {
        Class cClass;
        try {
            cClass = Class.forName(strClasse);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        T obj = (T) findByID(strId, cClass);
        return obj;
    }

    public <T> List<T> selectAll(Class classe) {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(classe));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public <T> List<T> selectBy(int[] intervalo, Class classe) {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(classe));
        Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(intervalo[1] - intervalo[0]);
        q.setFirstResult(intervalo[0]);
        return q.getResultList();
    }

    public int count(Class classe) {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root rt = cq.from(classe);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public <T> List<T> executeNamedQueryWithParams(String namedQuery, String[] parametros, Object[] objetos) {
        return executeNamedQueryWithParams(namedQuery, parametros, objetos, false);
    }

    public <T> List<T> executeNamedQueryWithParams(String namedQuery, String[] parametros, Object[] objetos, boolean singleResult) {
        Query query = getEntityManager().createNamedQuery(namedQuery);
        if (singleResult) {
            query.setFirstResult(0);
        }
        if (parametros.length != objetos.length) {
            throw new IndexOutOfBoundsException("The lenght of params array is not equals lenght of objects array.");
        }
        for (int i = 0; i < parametros.length; i++) {
            String atributo = parametros[i];
            Object parametro = objetos[i];
            query.setParameter(atributo, parametro);
        }
        List<T> list = query.getResultList();
        return list;
    }

    public <T> List<T> executeNamedQuery(String namedQuery) {
        List<T> list = getEntityManager().createNamedQuery(namedQuery).getResultList();
        return list;
    }

    public <T> List<T> selectWithParams(String select, String[] params, Object[] objects) {
        return selectWithParams(select, params, objects, 0, 0);
    }

    public <T> List<T> selectWithParams(String select, String[] params, Object[] objects, int offset, int limit) {
        Query query = em.createQuery(select);
        if (params.length != objects.length) {
            throw new IndexOutOfBoundsException("The lenght of params array is not equals lenght of objects array.");
        }
        for (int i = 0; i < params.length; i++) {
            if (params[i].equals("#none#")) {
                continue;
            }
            String atributo = params[i];
            Object parametro = objects[i];
            query.setParameter(atributo, parametro);
        }
        if (offset > 0) {
            query.setFirstResult(offset);
        }
        if (limit > 0) {
            query.setMaxResults(limit);
        }
        return query.getResultList();
    }

    public <T> List<T> selectWithParams(String select, String[] params, Object[] objects, Class<T> clazz) {
        return selectWithParams(select, params, objects, 0, 0, clazz);
    }

    public <T> List<T> selectWithParams(String select, String[] params, Object[] objects, int offset, int limit, Class<T> clazz) {
        TypedQuery<T> query = em.createQuery(select, clazz);
        if (params.length != objects.length) {
            throw new IndexOutOfBoundsException("The lenght of params array is not equals lenght of objects array.");
        }
        for (int i = 0; i < params.length; i++) {
            if (params[i].equals("#none#")) {
                continue;
            }
            String atributo = params[i];
            Object parametro = objects[i];
            query.setParameter(atributo, parametro);
        }
        if (offset > 0) {
            query.setFirstResult(offset);
        }
        if (limit > 0) {
            query.setMaxResults(limit);
        }
        return query.getResultList();
    }

    public List selectNativeWithParams(String select, Object[] objects) {
        Query query = em.createNativeQuery(select);

        if (objects != null) {
            for (int i = 0; i < objects.length; i++) {
                query.setParameter(i + 1, objects[i]);
            }
        }

        return query.getResultList();
    }

    public <T> T selectOneWithParams(String select, String[] params, Object[] objects) {
        List<T> result = selectWithParams(select, params, objects);
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    public <T> T selectNativeOneWithParams(String select, Object[] objects) {
        List<T> result = selectNativeWithParams(select, objects);
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }
}
