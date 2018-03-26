package com.enlightent.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

public class MyAliasToEntityMapResultTransformer extends AliasedTupleSubsetResultTransformer {
	
	public static final MyAliasToEntityMapResultTransformer INSTANCE = new MyAliasToEntityMapResultTransformer();

	/**
	 * Disallow instantiation of AliasToEntityMapResultTransformer.
	 */
	private MyAliasToEntityMapResultTransformer() {
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		Map result = new LinkedHashMap(tuple.length);
		for ( int i=0; i<tuple.length; i++ ) {
			String alias = aliases[i];
			if ( alias!=null ) {
				result.put( alias, tuple[i] );
			}
		}
		return result;
	}

	@Override
	public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
		return false;
	}

	/**
	 * Serialization hook for ensuring singleton uniqueing.
	 *
	 * @return The singleton instance : {@link #INSTANCE}
	 */
	private Object readResolve() {
		return INSTANCE;
	}
}
