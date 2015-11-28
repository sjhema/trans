package com.primovision.lutransport.core.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.primovision.lutransport.core.util.CrudUtil;
import com.primovision.lutransport.model.AuditLog;
import com.primovision.lutransport.model.Auditable;
import com.primovision.lutransport.model.BaseModel;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.service.AuditService;

/**
 */
@Transactional(readOnly = true)
@SuppressWarnings({ "unchecked" })
public class GenericJpaDAO implements GenericDAO {

	@Autowired
	private AuditService auditService;

	public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public <T extends BaseModel> T getById(Class<T> clazz, Long id) {
		return entityManager.find(clazz, id);
	}

	@Override
	public <T extends BaseModel> T getByCriteria(Class<T> clazz, Map criterias) {
		List<T> items = findByCriteria(clazz, criterias, null, false);
		if (items != null && items.size() > 0)
			return items.get(0);
		return null;
	}

	@Override
	public <T extends BaseModel> T getByUniqueAttribute(Class<T> clazz,
			String attributeName, Object value) {
		Map criterias = new HashMap();
		criterias.put(attributeName, value);
		List<T> items = findByCriteria(clazz, criterias);
		if (items != null && items.size() > 0)
			return items.get(0);
		return null;
	}

	@Override
	public <T extends BaseModel> T getByNamedQuery(String namedQuery,
			Map<String, Object> map) {
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
	public <T extends BaseModel> List<T> executeSimpleQuery(String query) {
		Query q = entityManager.createQuery(query);
		return q.getResultList();
	}
	
	
	@Override
	public <T extends BaseModel> void executeSimpleUpdateQuery(String query) {
		Query q = entityManager.createQuery(query);
		q.executeUpdate();
	}
	

	@Override
	public <T extends BaseModel> List<T> findAll(Class<T> clazz) {
		return findByCriteria(clazz, null);
	}

	@Override
	public <T extends BaseModel> List<T> findAll(Class<T> clazz,
			boolean activeOnly) {
		Map criterias = new HashMap();
		criterias.put("status", 1);
		return findByCriteria(clazz, criterias);
	}

	@Override
	public <T extends BaseModel> List<T> findByCriteria(Class<T> clazz,
			Map criterias) {
		return findByCriteria(clazz, criterias, null, false);
	}

	@Override
	public <T extends BaseModel> List<T> findByCriteria(Class<T> clazz,
			Map criterias, String sortField, boolean desc) {
		// Build the Query String with Search Criteria
		int count = 0;
		int counter = 0;
		StringBuilder query = new StringBuilder("Select obj from ").append(
				clazz.getSimpleName()).append(" obj where 1=1");
		if (criterias != null && criterias.size() > 0) {
			Object[] keyArray = criterias.keySet().toArray();
			for (int i = 0; i < keyArray.length; i++) {
				if (keyArray[i].toString().startsWith("||")) {
					counter++;
				}
			}
			if (counter == 1)
				query = new StringBuilder("Select obj from ").append(
						clazz.getSimpleName()).append(" obj where 1!=1");
			for (int i = 0; i < keyArray.length; i++) {
				if (keyArray[i].toString().startsWith("||")) {
					if (count == 0 && counter > 1)
						query.append(" and ( ");
					else
						query.append(" or ");
					if (criterias.get(keyArray[i]).toString().contains("null")) {
						if (criterias.get(keyArray[i]).toString().contains("!"))
							query.append(" obj.").append(keyArray[i])
									.append(" IS NOT NULL");
						else
							query.append(" obj.").append(keyArray[i])
									.append(" IS NULL");

					} else if (criterias.get(keyArray[i]).toString()
							.contains("<>")
							|| criterias.get(keyArray[i]).toString()
									.contains("!=")) {
						query.append(" obj.").append(keyArray[i]).append("!=:")
								.append("p" + i);
					} else if (criterias.get(keyArray[i]).toString()
							.contains(",")) {
						StringBuilder valBuilder = new StringBuilder();
						String[] values = criterias.get(keyArray[i]).toString()
								.trim().split(",");
						for (int j = 0; j < values.length; j++) {
							values[j] = "'" + values[j] + "',";
							valBuilder.append(values[j]);
						}
						if (valBuilder.length() > 1) {
							valBuilder = valBuilder.deleteCharAt(valBuilder
									.length() - 1);
							query.append(" obj.")
									.append(keyArray[i])
									.append(" IN (" + valBuilder.toString()
											+ ")");
						}
					} else
						query.append(" obj.")
								.append(keyArray[i].toString()
										.substring(
												keyArray[i].toString().indexOf(
														"|") + 2)).append("=:")
								.append("p" + i);
					count++;
				}

			}
			if (count > 1)
				query.append(" )");
			for (int i = 0; i < keyArray.length; i++) {
				if (keyArray[i].toString().startsWith("||")) {
					continue;
				} else if (criterias.get(keyArray[i]).toString()
						.contains("null")) {
					if (criterias.get(keyArray[i]).toString().contains("!"))
						query.append(" and obj.").append(keyArray[i])
								.append(" IS NOT NULL");
					else
						query.append(" and obj.").append(keyArray[i])
								.append(" IS NULL");
				} else if (criterias.get(keyArray[i]).toString().contains("<>")
						|| criterias.get(keyArray[i]).toString().contains("!=")) {
					query.append(" and obj.").append(keyArray[i]).append("!=:")
							.append("p" + i);
				} else if (criterias.get(keyArray[i]).toString().contains(">")) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"dd-MM-yyyy");
					String modifiedValue = criterias.get(keyArray[i])
							.toString().replace(">", "");
					try {
						criterias.put(keyArray[i],
								dateFormat.parse(modifiedValue));
					} catch (Exception e) {

						e.printStackTrace();
					}
					query.append(" and obj.").append(keyArray[i]).append(">:")
							.append("p" + i);
				} else if (criterias.get(keyArray[i]).toString().contains(",")) {
					StringBuilder valBuilder = new StringBuilder();
					String[] values = criterias.get(keyArray[i]).toString()
							.trim().split(",");
					for (int j = 0; j < values.length; j++) {
						values[j] = "'" + values[j] + "',";
						valBuilder.append(values[j]);
					}
					if (valBuilder.length() > 1) {
						valBuilder = valBuilder.deleteCharAt(valBuilder
								.length() - 1);
						query.append(" and obj.").append(keyArray[i])
								.append(" IN (" + valBuilder.toString() + ")");
					}
				} else if (keyArray[i].toString().equalsIgnoreCase("dateField")
						&& criterias.get("from") != null
						&& criterias.get("to") != null) {
					query.append(" and obj.")
							.append(criterias.get("dateField"));
					Timestamp firstDate = new Timestamp(
							((Date) criterias.get("from")).getTime());
					Timestamp secondDate = new Timestamp(
							((Date) criterias.get("to")).getTime() + 86399999);
					query.append(" Between '" + firstDate + "' AND '"
							+ secondDate + "'");
				} else {
					if (keyArray[i].toString().equalsIgnoreCase("dateField")) {
						// Do nothing
					} else if (keyArray[i].toString().equalsIgnoreCase("from")) {
						// Do nothing
					} else if (keyArray[i].toString().equalsIgnoreCase("to")) {
						// Do nothing
					} else
						query.append(" and obj.").append(keyArray[i])
								.append("=:").append("p" + i);
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
		// Set the search Parameters for the query
		if (criterias != null && criterias.size() > 0) {
			Object[] keyArray = criterias.keySet().toArray();
			for (int i = 0; i < keyArray.length; i++) {
				if (criterias.get(keyArray[i]).toString().contains("!="))
					jpaQuery.setParameter(
							"p" + i,
							criterias
									.get(keyArray[i])
									.toString()
									.substring(
											criterias.get(keyArray[i])
													.toString()
													.lastIndexOf("=") + 1));
				else if (criterias.get(keyArray[i]).toString().contains("null")) {
					// Do nothing
				} else if (criterias.get(keyArray[i]).toString().contains(",")) {
					// Do nothing
				} else if (keyArray[i].toString().equalsIgnoreCase("dateField")) {
					// Do nothing
				} else if (keyArray[i].toString().equalsIgnoreCase("from")) {
					// Do nothing
				} else if (keyArray[i].toString().equalsIgnoreCase("to")) {
					// Do nothing
				} else
					jpaQuery.setParameter("p" + i, criterias.get(keyArray[i]));
			}

		}
		// Execute the query and return List of objects
		return jpaQuery.getResultList();
	}

	@Override
	public <T extends BaseModel> List<T> findByNamedQuery(String namedQuery,
			Map<String, Object> map) {
		Query q = entityManager.createNamedQuery(namedQuery);
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			q.setParameter(key, map.get(key));
		}
		return q.getResultList();
	}

	@Override
	public <T extends BaseModel> List<T> findLimitedByCount(Class<T> clazz,
			Map criterias, Integer maxCount) {
		Boolean containsOr = false;
		int count = 0;
		StringBuffer queryCount = new StringBuffer("select count(p) from "
				+ clazz.getSimpleName() + " p where 1=1 ");
		StringBuffer searchString = new StringBuffer("");
		if (criterias != null) {
			searchString = createQueryFromCriterias(clazz, criterias,
					containsOr, count);
		}
		if (containsOr && count == 1)
			queryCount = new StringBuffer("select count(p) from "
					+ clazz.getSimpleName() + " p where 1!=1 ");
		queryCount.append(searchString.toString());
		StringBuffer queryStmt = new StringBuffer(" from "
				+ clazz.getSimpleName() + " p where 1=1 ");
		if (containsOr && count == 1)
			queryStmt = new StringBuffer(" from " + clazz.getSimpleName()
					+ " p where 1!=1 ");
		queryStmt.append(searchString.toString());
		return entityManager.createQuery(queryStmt.toString())
				.setMaxResults(maxCount).getResultList();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public <T extends BaseModel> void save(T entity) {
		entityManager.persist(entity);
		// writeEntityLog("ADD", entity, null);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public <T extends BaseModel> void saveOrUpdate(T entity) {
		System.out.println("Entity id = " + entity.getId());
		if (entity.getId() == null) {
			entityManager.persist(entity);
			// writeEntityLog("ADD", entity, null);
		} else {
			
			System.out.println("Last login date = " + ((com.primovision.lutransport.model.User)entity).getLastLoginDate());
			entityManager.flush();
			entityManager.merge(entity);
			
			Session session = ((Session) entityManager.getDelegate())
					.getSessionFactory().openSession();
			
			System.out.println("session opened?");
			T oldEntity = (T) session.get(entity.getClass(), entity.getId());
			System.out.println("session = " + session.isOpen());
			
			// writeEntityLog("UPDATE", entity, oldEntity);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public <T extends BaseModel> void delete(T entity) {
		entityManager.remove(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public <T extends BaseModel> void deleteById(Class<T> clazz, Long id) {
		entityManager.remove(getById(clazz, id));
	}

	@Override
	public <T extends BaseModel> List<T> search(Class<T> clazz,
			SearchCriteria criteria) {
		return search(clazz, criteria, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssms.core.dao.GenericDAO#search(java.lang.Class,
	 * com.ssms.models.SearchCriteria, java.lang.String, boolean)
	 */
	@Override
	public <T extends BaseModel> List<T> search(Class<T> clazz,
			SearchCriteria criteria, String orderField, Boolean desc) {
		return search(clazz, criteria, orderField, desc, null);
	}

	@Override
	public <T extends BaseModel> List<T> search(Class<T> clazz,
			SearchCriteria criteria, String orderField, Boolean desc,
			String groupField) {
		Boolean containsOr = false;
		int count = 0;
		StringBuffer queryCount = new StringBuffer("select count(p) from "
				+ clazz.getSimpleName() + " p where 1=1 ");
		StringBuffer searchString = new StringBuffer("");
		if (criteria != null && criteria.getSearchMap() != null) {
			searchString = createQueryFromCriterias(clazz,
					criteria.getSearchMap(), containsOr, count);
		}
		if (containsOr && count == 1)
			queryCount = new StringBuffer("select count(p) from "
					+ clazz.getSimpleName() + " p where 1!=1 ");
		queryCount.append(searchString.toString());
		if (groupField != null)
			searchString.append(" GROUP BY " + groupField);
		if (orderField != null)
			searchString.append(" ORDER BY " + orderField);
		else {
			// Setting default search result to be sorted by created at
			searchString.append(" ORDER BY createdAt DESC");
		}
		if (desc != null) {
			if (!desc)
				searchString.append(" asc");
			else
				searchString.append(" desc");
		}
		Long recordCount = (Long) entityManager.createQuery(
				queryCount.toString()).getSingleResult();
		criteria.setRecordCount(recordCount.intValue());
		StringBuffer queryStmt = new StringBuffer(" from "
				+ clazz.getSimpleName() + " p where 1=1 ");
		if (containsOr && count == 1)
			queryStmt = new StringBuffer(" from " + clazz.getSimpleName()
					+ " p where 1!=1 ");
		queryStmt.append(searchString.toString());
		System.out.println("***** the search string is "+queryStmt.toString());
		return entityManager.createQuery(queryStmt.toString())
				.setMaxResults(criteria.getPageSize())
				.setFirstResult(criteria.getPage() * criteria.getPageSize())
				.getResultList();
	}

	@Override
	public <T extends BaseModel> boolean isUnique(Class<T> clazz, T entity,
			Map properties) {
		StringBuffer query = new StringBuffer("select obj from "
				+ clazz.getSimpleName() + " obj where 1=1");
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
		long id = -1l;
		if (entity.getId() != null) {
			id = entity.getId();
			query.append(" and obj.id != " + id);
		}		
		
		System.out.println("****** the query string is "+query.toString());
		List<T> resultList = entityManager.createQuery(query.toString())
				.getResultList();
		System.out.println("********* the result is --->> "+resultList.size());
		if (resultList != null && resultList.size() > 0) {
			return false;
		}
		return true;
	}

	@Override
	public <T extends BaseModel> List<T> searchByDate(Class<T> clazz,
			SearchCriteria criteria) {
		StringBuffer queryCount = new StringBuffer("select p from "
				+ clazz.getSimpleName() + " p");
		StringBuffer searchString = new StringBuffer("");
		if (criteria != null && criteria.getSearchMap() != null) {
			Object[] param = criteria.getSearchMap().keySet().toArray();
			for (int i = 0; i < param.length; i++) {
				if (criteria.getSearchMap().get(param[i]) != null)
					if (!criteria.getSearchMap().get(param[i]).equals("")) {
						if (i == 0)
							searchString.append(" where p.createdAt BETWEEN ");
						searchString.append(" '"
								+ criteria.getSearchMap().get(param[i])
								+ "' and ");
						if (i == 1)
							searchString.append("1=1");
					}
			}
			queryCount.append(searchString.toString());
		}
		Query q = entityManager.createQuery(queryCount.toString());
		return q.getResultList();
	}

	@Override
	public <T extends BaseModel> List<T> findByCriteria(Class<T> clazz,
			Map criterias, boolean orderResult, String sortField, boolean desc,
			String groupField) {
		// Build the Query String with Search Criteria
		StringBuilder query = new StringBuilder("Select obj from ").append(
				clazz.getSimpleName()).append(" obj where 1=1");
		if (criterias != null && criterias.size() > 0) {
			Object[] keyArray = criterias.keySet().toArray();
			for (int i = 0; i < keyArray.length; i++) {
				if (criterias.get(keyArray[i]).toString().contains("null")) {
					if (criterias.get(keyArray[i]).toString().contains("!"))
						query.append(" and obj.").append(keyArray[i])
								.append(" IS NOT NULL");
					else
						query.append(" and obj.").append(keyArray[i])
								.append(" IS NULL");

				} else {
					query.append(" and obj.").append(keyArray[i]).append("=:")
							.append("p" + i);
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
		int index = 0;
		if (criterias != null && criterias.size() > 0) {
			Object[] keyArray = criterias.keySet().toArray();
			for (int i = 0; i < keyArray.length; i++) {
				if (!criterias.get(keyArray[i]).toString().contains("null")) {
					index++;
				}
			}
		}
		Query jpaQuery = entityManager.createQuery(query.toString());
		// Set the search Parameters for the query
		if (criterias != null && criterias.size() > 0) {
			Object[] keyArray = criterias.keySet().toArray();
			for (int i = 0; i < keyArray.length; i++) {
				if (criterias.get(keyArray[i]).toString().contains("!="))
					jpaQuery.setParameter(
							"p" + i,
							criterias
									.get(keyArray[i])
									.toString()
									.substring(
											criterias.get(keyArray[i])
													.toString()
													.lastIndexOf("=") + 1));
				else if (criterias.get(keyArray[i]).toString().contains("null")) {
					// Do nothing
				} else if (criterias.get(keyArray[i]).toString().contains(",")) {
					// Do nothing
				} else if (keyArray[i].toString().equalsIgnoreCase("dateField")) {
					// Do nothing
				} else if (keyArray[i].toString().equalsIgnoreCase("from")) {
					// Do nothing
				} else if (keyArray[i].toString().equalsIgnoreCase("to")) {
					// Do nothing
				} else
					jpaQuery.setParameter("p" + i, criterias.get(keyArray[i]));
			}

		}
		// Execute the query and return List of objects
		return jpaQuery.getResultList();
	}

	@Override
	public <T extends BaseModel> List<T> searchByQuery(Class<T> clazz,
			SearchCriteria criteria, String query, String countQuery) {
		StringBuffer queryCount = new StringBuffer(countQuery);
		Long recordCount = (Long) entityManager.createQuery(
				queryCount.toString()).getSingleResult();
		criteria.setRecordCount(recordCount.intValue());
		StringBuffer queryStmt = new StringBuffer(query);
		return entityManager.createQuery(queryStmt.toString())
				.setMaxResults(criteria.getPageSize())
				.setFirstResult(criteria.getPage() * criteria.getPageSize())
				.getResultList();
	}

	@Override
	public <T extends BaseModel> List<T> findByCommaSeparatedIds(
			Class<T> clazz, String ids) {
		StringBuilder query = new StringBuilder("Select obj from ").append(
				clazz.getSimpleName()).append(" obj where ");
		query.append(" obj.id").append(" IN (" + ids + ")");
		return entityManager.createQuery(query.toString()).getResultList();
	}

	private <T extends BaseModel> StringBuffer createQueryFromCriterias(
			Class<T> clazz, Map criterias, Boolean containsOr, int count) {
		int counter = 0;
		StringBuffer searchString = new StringBuffer("");
		if (criterias != null) {
			for (Object param : criterias.keySet()) {
				if (param.toString().startsWith("||"))
					counter++;
			}
			for (Object param : criterias.keySet()) {
				if (criterias.get(param.toString())!=null) {
					if (param.toString().startsWith("||")) {
						containsOr = true;
						if (count == 0 && counter > 1)
							searchString.append(" and ( ");
						else
							searchString.append(" or ");
						String criteriaKey = param.toString().replace("||", "");
						if (criterias.get(param.toString()) instanceof Integer
								|| criterias.get(param.toString()) instanceof Long) {
							searchString.append(" p." + criteriaKey + "="
									+ criterias.get(param.toString()));
						}
						else if (!criterias.get(param.toString()).toString()
								.contains("null")) {
							if (criterias.get(param.toString()).toString()
									.contains("!"))
								searchString.append(" p." + criteriaKey
										+ " IS NOT NULL");
							else
								searchString.append(" p." + criteriaKey
										+ " IS NULL");
						} else if (criterias.get(param.toString()).toString()
								.contains(",")) {
							StringBuilder valBuilder = new StringBuilder();
							String[] values = criterias.get(param.toString())
									.toString().trim().split(",");
							for (int i = 0; i < values.length; i++) {
								values[i] = "'" + values[i] + "',";
								valBuilder.append(values[i]);
							}
							if (valBuilder.length() > 1) {
								valBuilder = valBuilder.deleteCharAt(valBuilder
										.length() - 1);
								searchString.append(" UPPER(p." + criteriaKey
										+ ") IN (" + valBuilder.toString()
										+ ")");
							}
						} else if (criterias.get(param.toString()).toString()
								.startsWith("!=")
								|| criterias.get(param.toString()).toString()
										.startsWith("<>")) {
							searchString.append(" p."
									+ criteriaKey
									+ " <> '"
									+ criterias.get(param.toString())
											.toString().trim().substring(2)
									+ "'");

						} else if (criterias.get(param.toString()) instanceof Date) {
							if ("from".equalsIgnoreCase(param.toString())) {
								Timestamp firstDate = new Timestamp(
										((Date) criterias.get(param.toString()))
												.getTime());
								Timestamp secondDate = null;
								secondDate = new Timestamp(
										((Date) criterias.get("to")).getTime() + 86399999);
								String fieldName = (String) criterias
										.get("dateField");
								searchString.append(" UPPER(p." + fieldName
										+ ") Between '" + firstDate + "' AND '"
										+ secondDate + "'");
							}
						} else {							
							if (!"dateField".equalsIgnoreCase(param.toString()))
								searchString.append(" UPPER(p." + criteriaKey
										+ ") like UPPER('"
										+ criterias.get(param.toString())
										+ "%') ");
						}
						count++;
					}
				}
			}
			if (count > 1)
				searchString.append(" )");
			for (Object param : criterias.keySet()) {
				if (param.toString().startsWith("||"))
					continue;
				else {
					if (criterias.get(param.toString()) instanceof Integer
							|| criterias.get(param.toString()) instanceof Long) {
						searchString.append(" and p." + param + "="
								+ criterias.get(param.toString()));
					}
					else if (StringUtils.isEmpty(criterias.get(param.toString()).toString())) {
						searchString.append("");
					}
					else if (criterias.get(param.toString()).toString()
							.contains("null")) {
						if (criterias.get(param.toString()).toString()
								.contains("!"))
							searchString.append(" and p." + param
									+ " IS NOT NULL");
						else
							searchString.append(" and p." + param + " IS NULL");
					} else if (criterias.get(param.toString()).toString()
							.contains(",")) {
						StringBuilder valBuilder = new StringBuilder();
						String[] values = criterias.get(param.toString())
								.toString().trim().split(",");
						for (int i = 0; i < values.length; i++) {
							values[i] = "'" + values[i] + "',";
							valBuilder.append(values[i]);
						}
						if (valBuilder.length() > 1) {
							valBuilder = valBuilder.deleteCharAt(valBuilder
									.length() - 1);
							searchString.append(" and UPPER(p." + param
									+ ") IN (" + valBuilder.toString() + ")");
						}
					} else if (criterias.get(param.toString()).toString()
							.startsWith("!=")
							|| criterias.get(param.toString()).toString()
									.startsWith("<>")) {
						searchString.append(" and p."
								+ param
								+ " <> '"
								+ criterias.get(param.toString()).toString()
										.trim().substring(2) + "'");

					} else if (criterias.get(param.toString()) instanceof Date) {
						if ("from".equalsIgnoreCase(param.toString())) {
							Timestamp firstDate = new Timestamp(
									((Date) criterias.get(param.toString()))
											.getTime());
							Timestamp secondDate = null;
							secondDate = new Timestamp(
									((Date) criterias.get("to")).getTime() + 86399999);
							String fieldName = (String) criterias
									.get("dateField");
							searchString.append(" and UPPER(p." + fieldName
									+ ") Between '" + firstDate + "' AND '"
									+ secondDate + "'");
						}
					} else {
						if (!"dateField".equalsIgnoreCase(param.toString()))
						{							
							if (criterias.get(param).toString().contains("-") )
							{
								if("weekOfDate".equalsIgnoreCase(param.toString())){									
									searchString.append(" and UPPER(p.weekOfDate"
											+ ") >= UPPER('"
											+ criterias.get(param.toString()) + " 00:00:00') ");
								}
								else if ("weekOfDateTo".equalsIgnoreCase(param.toString())){
									searchString.append(" and UPPER(p.weekOfDate"
											+ ") <= UPPER('"
											+ criterias.get(param.toString()) + " 00:00:00') ");
								}
								else if ("weekDate".equalsIgnoreCase(param.toString())){
									searchString.append(" and UPPER(p.weekDate"
											+ ") >= UPPER('"
											+ criterias.get(param.toString()) + " 00:00:00') ");
								}
								else if ("weekDateTo".equalsIgnoreCase(param.toString())){
									searchString.append(" and UPPER(p.weekDate"
											+ ") <= UPPER('"
											+ criterias.get(param.toString()) + " 00:00:00') ");
								}
								else{
									searchString.append(" and UPPER(p." + param
											+ ") like UPPER('"
											+ criterias.get(param.toString()) + "%') ");
								}
							}
							else{
								searchString.append(" and UPPER(p." + param
										+ ") like UPPER('"
										//+ criterias.get(param.toString()) + "%') ");
										 + criterias.get(param.toString()) + "') ");
							}
						}
					}
				}
				
			}
		}
		return searchString;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public <T extends BaseModel> void writeEntityLog(String activityType,
			T entity, T oldEntity) {
		if (!(entity instanceof Auditable)) {
			AuditLog auditLog = new AuditLog();
			auditLog.setCreatedAt(Calendar.getInstance().getTime());
			auditLog.setReference(activityType);
			auditLog.setEntityClass(entity.getClass().getSimpleName());
			if ("ADD".equalsIgnoreCase(activityType))
				auditLog.setMessage(CrudUtil.buildCreateMessage(entity));
			if ("UPDATE".equalsIgnoreCase(activityType))
				auditLog.setMessage(CrudUtil.buildUpdateMessage(entity,
						oldEntity));
			if ("DELETE".equalsIgnoreCase(activityType))
				auditLog.setMessage(CrudUtil.buildDeleteMessage(entity));
			if (SecurityContextHolder.getContext().getAuthentication() != null) {
				auditLog.setUserId(SecurityContextHolder.getContext()
						.getAuthentication().getName());
			}
			saveOrUpdate(auditLog);
		}
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

}
