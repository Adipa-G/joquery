package joquery.core.collection.project;

import joquery.IProject;

import java.util.function.Function;

/**
 * Created by adipa_000 on 2/14/2015.
 */
public class Sum<T,U extends Number> implements IProject<T,U,U>
{
	@Override
	public U eval(Iterable<T> tList, Function<T,U> property)
	{
		U total = null;
		for (T t : tList)
		{
			total = add(property.apply(t),total);
		}
		return total;
	}

	private U add(U u1,U u2)
	{
		if (u2 == null)
			return u1;
		if (u1 instanceof Integer)
			return (U)(Integer)(((Integer)u1) + ((Integer)u2));
		if (u1 instanceof Long)
			return (U)(Long)(((Long)u1) + ((Long)u2));
		return (U)(Double)(((Double)u1) + ((Double)u2));
	}
}
