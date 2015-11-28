package com.primovision.lutransport.core.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.primovision.lutransport.model.BaseModel;
import com.primovision.lutransport.model.SearchCriteria;

/**
 */
@SuppressWarnings("unchecked")
public interface GenericDAO {

    <T extends BaseModel> T getById(Class<T> clazz, Long id);

    <T extends BaseModel> T getByCriteria(Class<T> clazz, Map criterias);

    <T extends BaseModel> T getByNamedQuery(String namedQuery, Map<String, Object> map);

    <T extends BaseModel> T getByUniqueAttribute(Class<T> clazz, String attributeName, Object value);

    <T extends BaseModel> List<T> findAll(Class<T> clazz);

    <T extends BaseModel> List<T> findAll(Class<T> clazz, boolean activeOnly);

    <T extends BaseModel> List<T> findByCriteria(Class<T> clazz, Map criterias);

    <T extends BaseModel> List<T> findByCriteria(Class<T> clazz, Map criterias, String orderField, boolean desc);

    <T extends BaseModel> List<T> findByCriteria(Class<T> clazz, Map criterias, boolean orderResult, String orderField, boolean desc,
	    String groupField);

    <T extends BaseModel> List<T> findByNamedQuery(String namedQuery, Map<String, Object> map);

    <T extends BaseModel> List<T> findLimitedByCount(Class<T> clazz, Map criterias, Integer maxCount);

    <T extends BaseModel> void save(T entity);

    <T extends BaseModel> void saveOrUpdate(T entity);

    <T extends BaseModel> void delete(T entity);

    <T extends BaseModel> void deleteById(Class<T> clazz, Long id);

    <T extends BaseModel> List<T> search(Class<T> clazz, SearchCriteria criteria);

    <T extends BaseModel> List<T> search(Class<T> clazz, SearchCriteria criteria, String orderField, Boolean desc);

    <T extends BaseModel> List<T> search(Class<T> clazz, SearchCriteria criteria, String orderField, Boolean desc, String groupField);

    <T extends BaseModel> List<T> searchByDate(Class<T> clazz, SearchCriteria criteria);

    <T extends BaseModel> List<T> searchByQuery(Class<T> clazz, SearchCriteria criteria, String query, String countQuery);

    <T extends BaseModel> List<T> executeSimpleQuery(String query);
    
    <T extends BaseModel> void executeSimpleUpdateQuery(String query);

    <T extends BaseModel> boolean isUnique(Class<T> clazz, T entity, Map properties);

    <T extends BaseModel> List<T> findByCommaSeparatedIds(Class<T> clazz, String ids);
    
    EntityManager getEntityManager();
}