package joquery;

import joquery.core.collection.project.Avg;
import joquery.core.collection.project.Sum;

/**
 * Created by adipa_000 on 2/14/2015.
 */
public class Projections
{
	public static  <T,U extends Number> IProject<T,U,U> Sum()
	{
		return new Sum<T,U>();
	}

	public static <T,U extends Number,V extends Number> IProject<T,U,V>  Avg()
	{
		return new Avg<T,U,V>();
	}
}
