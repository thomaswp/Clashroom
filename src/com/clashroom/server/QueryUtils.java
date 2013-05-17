package com.clashroom.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.clashroom.shared.Debug;
import com.clashroom.shared.entity.UserEntity;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

/**
 * Static class to help query the datastore.
 */
public class QueryUtils {
	
	@Deprecated
	public static <T> T queryUnique(Class<T> c, String filter, Object... parameters) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		T answer = queryUnique(pm, c, filter, parameters);
		pm.close();
		return answer;
	}
	
	/**
	 * Queries the datastore for a unique instance of the given Class,
	 * meeting the given parameters.
	 * <p/>
	 * Filters should take the form found at
	 * <a href="https://developers.google.com/appengine/docs/java/datastore/jdo/queries#Filters">
	 * https://developers.google.com/appengine/docs/java/datastore/jdo/queries#Filters</a>
	 * but instead of declaring parameters to compare field against, just write "%s" for each
	 * parameter. Then fill them in using the parameters. For example:
	 * queryUnique(pm, UserEntity.class, "id==%s", 5) would return the {@link UserEntity}
	 * with an id of 5. You could of course replace 5 with a variable.
	 * 
	 * @param pm The PersistenceManager to use
	 * @param c The Class for which to query
	 * @param filter 
	 * @param parameters 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T queryUnique(PersistenceManager pm, 
			Class<T> c, String filter, Object... parameters) {
		return (T)query(pm, c, true, filter, parameters);
	}
	
	@Deprecated
	public static <T> List<T> query(Class<T> c, String filter, Object... parameters) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<T> answer = query(pm, c, filter, parameters);
		LinkedList<T> copy = new LinkedList<T>();
		copy.addAll(answer);
		pm.close();
		return copy;
	}
	
	/**
	 * Queries the datastore, returning a list of all Entities which fit the given filter. For more
	 * on writing filters, see {@link QueryUtils#queryUnique(Class, String, Object...)}.
	 * 
	 * @param pm The PersistenceManager to use
	 * @param c The Class for which to query
	 * @param filter
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> query(PersistenceManager pm, 
			Class<T> c, String filter, Object... parameters) {
		return (List<T>)query(pm, c, false, filter, parameters);
	}
	
	/**
	 * Queries the datastore for a given number of results of the
	 * given Class, using the given ordering. Returns a cursorString
	 * which can be passed to this method to pick up returning results
	 * where the last query left off.
	 * 
	 * @param pm The PersistenceManager
	 * @param c The Class for which to query
	 * @param ordering The field to use for ordering, for insance "date asc" to
	 * use the date field of an Entity to order the results, in ascending order
	 * @param numResults The number of results to return
	 * @param cursorString The cursor String
	 * @return The cursor String to pass to the next query to pick up where this
	 * query left off
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> queryRange(PersistenceManager pm,
			Class<T> c, String ordering, int numResults, String cursorString) {
		
		Query query = pm.newQuery(c);
		query.setRange(0, numResults);
		query.setOrdering(ordering);
		
		if (cursorString != null) {
			Cursor cursor = Cursor.fromWebSafeString(cursorString);
			HashMap<String, Object> extensionMap = new HashMap<String, Object>();
			extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
			query.setExtensions(extensionMap);
		}
		
		return (List<T>)query.execute();
	}

	/**
	 * 
	 * @param pm
	 * @param c
	 * @param unique
	 * @param filter Write a filter using %s as a standin for each parameter
	 * @param parameters List parameters using actual data type (not toString)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> Object query(PersistenceManager pm, 
			Class<T> c, boolean unique, String filter, Object... parameters) {
		Object[] peqs = new String[parameters.length];
		String declaredParams = "";
		for (int i = 0; i < peqs.length; i++) {
			peqs[i] = "p" + i;
			
			if (parameters[i] == null) {
				if (unique) return null;
				else return new LinkedList<T>();
			}
			
			if (declaredParams.length() > 0) declaredParams += ", ";
			declaredParams += parameters[i].getClass().getName() + " " +
					peqs[i];
		}
		
		Query q = pm.newQuery(c);
		q.setFilter(String.format(filter, peqs));
		q.declareParameters(declaredParams);
		q.setUnique(unique);
		
		
		Object result;
		
		String query = null;
		if (Debug.DEBUG) {
			Object[] params = new String[parameters.length];
			for (int i = 0; i < params.length; i++) params[i] = parameters[i].toString();
			query = String.format(filter, params);
		}
		
		if (parameters.length == 0) {
			result = q.execute();
		} else if (parameters.length == 1) {
			result = q.execute(parameters[0]);
		} else if (parameters.length == 2) {
			result = q.execute(parameters[0], parameters[1]);
		} else if (parameters.length == 3) {
			result = q.execute(parameters[0], parameters[1], parameters[2]);
		} else {
			Debug.write("Search (%s): '%s' fails", c.getSimpleName(), query);
			return null;
		}
		
		if (Debug.DEBUG) {
			Debug.write("Search (%s): '%s' yields: %s", c.getSimpleName(), query, 
					result == null ? "null" : result.toString());
		}
		
		if (unique) {
			return (T)result;
		} else {
			return (List<T>)result;
		}
		
	}
}
