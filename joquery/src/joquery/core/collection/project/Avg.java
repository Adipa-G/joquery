package joquery.core.collection.project;

import joquery.IProject;

import java.util.function.Function;

/**
 * Created by adipa_000 on 2/14/2015.
 */
public class Avg<T,U extends Number,V extends Number> implements IProject<T,U,V>
{
	@Override
	public V eval(Iterable<T> tList, Function<T, U> property)
	{
		U total = null;
		int count = 0;
		for (T t : tList)
		{
			total = add(property.apply(t),total);
			count++;
		}

		if (count == 0)
			return (V)total;

		return devide(total,count);
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

	private V devide(U u,int count)
	{
		if (u instanceof Integer)
			return (V)(Integer)(((Integer)u) / count);
		if (u instanceof Long)
			return (V)(Long)(((Long)u) /count);
		return (V)(Double)(((Double)u) / count);
	}
}
