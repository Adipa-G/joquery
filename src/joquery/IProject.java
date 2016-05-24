package joquery;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

/**
 * Created by adipa_000 on 2/14/2015.
 */
public interface IProject<T,U,V>
{
	V eval(Iterable<T> tList,Function<T,U> property);
}
