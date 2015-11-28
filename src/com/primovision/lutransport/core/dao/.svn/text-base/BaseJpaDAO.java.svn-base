package com.primovision.lutransport.core.dao;

import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.SearchCriteria;

/**
 * 
 */
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class BaseJpaDAO<T extends AbstractBaseModel> implements BaseDAO<T> {

    private final Class<T> clazz;

    public BaseJpaDAO() {
	this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @PersistenceContext
    protected EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
	this.entityManager = entityManager;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(T entity) {
	entityManager.remove(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteById(Long id) {
	entityManager.remove(getById(id));
    }

    @Override
    public List<T> executeSimpleQuery(String query) {
	Query q = entityManager.createQuery(query);
	return q.getResultList();
    }

    @Override
    public List<T> findAll() {
	return findAll(false);
    }

    @Override
    public List<T> findAll(boolean activeOnly) {
	Map criterias = null;
	if (activeOnly) {
	    criterias = new HashMap();
	    criterias.put("status", 1);
	}
	return findByCriteria(criterias);
    }

    @Override
    public List<T> findByCriteria(Map criterias) {
	return findByCriteria(criterias, null, false);
    }

    @Override
    public List<T> findByCriteria(Map criterias, String sortField, boolean desc) {
	// Build the Query String with Search Criteria
	StringBuilder query = new StringBuilder("Select obj from ").append(clazz.getSimpleName()).append(" obj where 1=1");
	if (criterias != null && criterias.size() > 0) {
	    Object[] keyArray = criterias.keySet().toArray();
	    for (int i = 0; i < keyArray.length; i++) {
		if (criterias.get(keyArray[i]).toString().contains("null")) {
		    if (criterias.get(keyArray[i]).toString().contains("!"))
			query.append(" and obj.").append(keyArray[i]).append(" IS NOT NULL");
		    else
			query.append(" and obj.").append(keyArray[i]).append(" IS NULL");
		} else {
		    query.append(" and obj.").append(keyArray[i]).append("=:").append("p" + i);
		}
	    }
	}
	if (sortField != null) {
	    query.append("  ORDER BY obj.").append(sortField);
	    if (!desc)
		query.append(" asc");
	    else
		query.append(" desc");

	}
	// Build the query Object
	Query jpaQuery = entityManager.createQuery(query.toString());
	// Set the search Parameters for the jpaQuery
	if (criterias != null && criterias.size() > 0) {
	    Object[] keyArray = criterias.keySet().toArray();
	    for (int i = 0; i < keyArray.length; i++) {
		if (!criterias.get(keyArray[i]).toString().contains("null"))
		    jpaQuery.setParameter("p" + i, criterias.get(keyArray[i]));
	    }

	}
	// Execute the query and return List of objects
	return jpaQuery.getResultList();
    }

    @Override
    public List<T> findByCriteria(Map criterias, boolean orderResult, String sortField, boolean desc, String groupField) {
	// Build the Query String with Search Criteria
	StringBuilder query = new StringBuilder("Select obj from ").append(clazz.getSimpleName()).append(" obj where 1=1");
	if (criterias != null && criterias.size() > 0) {
	    Object[] keyArray = criterias.keySet().toArray();
	    for (int i = 0; i < keyArray.length; i++) {
		if (criterias.get(keyArray[i]).toString().contains("null")) {
		    if (criterias.get(keyArray[i]).toString().contains("!"))
			query.append(" and obj.").append(keyArray[i]).append(" IS NOT NULL");
		    else
			query.append(" and obj.").append(keyArray[i]).append(" IS NULL");

		} else {
		    query.append(" and obj.").append(keyArray[i]).append("=:").append("p" + i);
		}

	    }
	}
	if (orderResult) {
	    if (sortField != null) {
		query.append("  ORDER BY obj.").append(sortField);
		if (!desc)
		    query.append(" asc");
		else
		    query.append(" desc");

	    }
	}
	if (groupField != null) {
	    query.append("  GROUP BY obj.").append(groupField);
	}
	// Build the query Object
	Query jpaQuery = entityManager.createQuery(query.toString());
	// Set the search Parameters for the jpaQuery
	if (criterias != null && criterias.size() > 0) {
	    Object[] keyArray = criterias.keySet().toArray();
	    for (int i = 0; i < keyArray.length; i++)
		if (!criterias.get(keyArray[i]).toString().contains("null"))
		    jpaQuery.setParameter("p" + i, criterias.get(keyArray[i]));
	}
	// Execute the query and return List of objects
	return jpaQuery.getResultList();
    }

    @Override
    public T getById(Long id) {
	return entityManager.find(clazz, id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void save(T entity) {
	entityManager.persist(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveOrUpdate(T entity) {
	if (entity.getId() == null) {
	    entityManager.persist(entity);
	} else {
	    entityManager.merge(entity);
	}
    }

    @Override
    public List<T> search(SearchCriteria criteria) {
	StringBuffer queryCount = new StringBuffer("select count(p) from " + clazz.getSimpleName() + " p where 1=1 ");
	StringBuffer searchString = new StringBuffer("");
	if (criteria != null && criteria.getSearchMap() != null) {
	    for (Object param : criteria.getSearchMap().keySet()) {
		if (criteria.getSearchMap().get(param.toString()) != null) {
		    if (criteria.getSearchMap().get(param.toString()) instanceof Integer
			    || criteria.getSearchMap().get(param.toString()) instanceof Long) {
			searchString.append(" and p." + param + "=" + criteria.getSearchMap().get(param.toString()));
		    } else if (criteria.getSearchMap().get(param.toString()).toString().equalsIgnoreCase("null")) {
			searchString.append(" and p." + param + " IS NULL");
		    } else if (criteria.getSearchMap().get(param.toString()).toString().contains(",")) {
			StringBuilder valBuilder = new StringBuilder();
			String[] values = criteria.getSearchMap().get(param.toString()).toString().trim().split(",");
			for (int i = 0; i < values.length; i++) {
			    values[i] = "'" + values[i] + "',";
			    valBuilder.append(values[i]);
			}
			if (valBuilder.length() > 1) {
			    valBuilder = valBuilder.deleteCharAt(valBuilder.length() - 1);
			    searchString.append(" and UPPER(p." + param + ") IN (" + valBuilder.toString() + ")");
			}
		    } else if (criteria.getSearchMap().get(param.toString()).toString().startsWith("!=")
			    || criteria.getSearchMap().get(param.toString()).toString().startsWith("<>")) {
			searchString.append(" and p." + param + " <> '"
				+ criteria.getSearchMap().get(param.toString()).toString().trim().substring(2) + "'");
		    } else if (criteria.getSearchMap().get(param.toString()) instanceof Date) {
			if ("from".equalsIgnoreCase(param.toString())) {
			    Timestamp firstDate = new Timestamp(((Date) criteria.getSearchMap().get(param.toString())).getTime());
			    Timestamp secondDate = null;
			    secondDate = new Timestamp(((Date) criteria.getSearchMap().get("to")).getTime() + 86399999);
			    String fieldName = (String) criteria.getSearchMap().get("dateField");
			    searchString.append(" and UPPER(p." + fieldName + ") Between '" + firstDate + "' AND '" + secondDate + "'");
			}
		    } else {
			if (!"dateField".equalsIgnoreCase(param.toString()))
			    searchString.append(" and UPPER(p." + param + ") like UPPER('" + criteria.getSearchMap().get(param.toString()) + "%') ");
		    }
		}
	    }
	}
	// Setting default search result to be sorted by created at
	searchString.append(" ORDER BY createdAt DESC");
	queryCount.append(searchString.toString());
	Long recordCount = (Long) entityManager.createQuery(queryCount.toString()).getSingleResult();
	criteria.setRecordCount(recordCount.intValue());
	StringBuffer queryStmt = new StringBuffer(" from " + clazz.getSimpleName() + " p where 1=1 ");
	queryStmt.append(searchString.toString());
	return entityManager.createQuery(queryStmt.toString()).setMaxResults(criteria.getPageSize()).setFirstResult(
		criteria.getPage() * criteria.getPageSize()).getResultList();
    }

    @Override
    public T getByNamedQuery(String namedQuery, Map<String, Object> map) {

	Query q = entityManager.createNamedQuery(namedQuery);
	Iterator<String> it = map.keySet().iterator();
	while (it.hasNext()) {
	    String key = it.next();
	    q.setParameter(key, map.get(key));

	}
	List<T> data = q.getResultList();
	if (data != null && data.size() > 0)
	    return data.get(0);
	else
	    return null;
    }

    @Override
    public List<T> findByNamedQuery(String namedQuery, Map<String, Object> map) {
	Query q = entityManager.createNamedQuery(namedQuery);
	Iterator<String> it = map.keySet().iterator();
	while (it.hasNext()) {
	    String key = it.next();
	    q.setParameter(key, map.get(key));
	}
	return q.getResultList();
    }

    @Override
    public boolean isUnique(T entity, Map properties) {
	StringBuffer query = new StringBuffer("select obj from " + clazz.getSimpleName() + " obj where 1=1");
	Iterator<String> it = properties.keySet().iterator();
	while (it.hasNext()) {
	    String key = it.next();
	    query.append(" and ");
	    if (properties.get(key) == null) {
		query.append("obj." + key + " is null");
	    } else {
		query.append("obj." + key + "='" + properties.get(key) + "'");
	    }
	}
	Query q = entityManager.createQuery(query.toString());
	List<T> resultList = q.getResultList();
	if (resultList != null && resultList.size() > 0) {
	    return false;
	}
	return true;
    }

    @Override
    public List<T> search(SearchCriteria criteria, boolean activeOnly) {
	Map criterias = null;
	if (activeOnly) {
	    criterias = new HashMap();
	    criterias.put("status", 1);
	}
	return search(criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ssms.core.dao.BaseDAO#isPresent(java.util.Map)
     */
    @Override
    public boolean isPresent(Map properties) {
	StringBuffer query = new StringBuffer("select obj from " + clazz.getSimpleName() + " obj where 1=1");
	Iterator<String> it = properties.keySet().iterator();
	while (it.hasNext()) {
	    String key = it.next();
	    query.append(" and ");
	    if (properties.get(key) == null) {
		query.append("obj." + key + " is null");
	    } else {
		query.append("obj." + key + "='" + properties.get(key) + "'");
	    }
	}
	Query q = entityManager.createQuery(query.toString());
	List<T> resultList = q.getResultList();
	if (resultList != null && resultList.size() > 0) {
	    return true;
	}
	return false;
    }
}
