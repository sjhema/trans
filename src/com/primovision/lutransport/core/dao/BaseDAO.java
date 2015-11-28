package com.primovision.lutransport.core.dao;

import java.util.List;
import java.util.Map;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.SearchCriteria;

/**
 * 
 */
@SuppressWarnings("unchecked")
public interface BaseDAO<T extends AbstractBaseModel> {

    T getById(Long id);

    List<T> findAll();

    List<T> findAll(boolean activeOnly);

    List<T> findByCriteria(Map criterias);

    void save(T entity);

    void saveOrUpdate(T entity);

    void delete(T entity);

    void deleteById(Long id);

    List<T> search(SearchCriteria criteria);

    List<T> search(SearchCriteria criteria, boolean activeOnly);

    List<T> executeSimpleQuery(String query);

    List<T> findByCriteria(Map criterias, String orderField, boolean desc);

    List<T> findByCriteria(Map criterias, boolean orderResult, String orderField, boolean desc, String groupField);

    T getByNamedQuery(String namedQuery, Map<String, Object> map);

    List<T> findByNamedQuery(String namedQuery, Map<String, Object> map);

    boolean isUnique(T entity, Map properties);

    boolean isPresent(Map properties);
}