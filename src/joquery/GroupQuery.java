package joquery;

import joquery.core.QueryException;

import java.util.Collection;
import java.util.function.Function;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 11:19 AM
 */
public interface GroupQuery<Key,U> extends Query<Grouping<Key,U>,GroupQuery<Key,U>>
{
    Collection<Grouping<Key,U>> list() throws QueryException;

    GroupQuery<Key,U> groupBy(Function<U,?> by);

    GroupQuery<Key,U> groupBy(String by);
}
