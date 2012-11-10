package joquery.core.collection;

import joquery.Exec;
import joquery.GroupQuery;
import joquery.Grouping;

/**
 * User: Adipa
 * Date: 11/10/12
 * Time: 7:05 PM
 */
public class GroupQueryImpl<Key,U> extends ResultTransformedQueryImpl<U,Grouping<Key,U>,GroupQuery<Key,U>>  implements GroupQuery<Key,U>
{
    @Override
    public GroupQuery<Key, U> groupBy(Exec<U> by)
    {
        return this;
    }

    @Override
    public GroupQuery<Key, U> groupBy(String by)
    {
        return this;
    }
}
