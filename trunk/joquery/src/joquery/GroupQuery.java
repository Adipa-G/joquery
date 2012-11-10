package joquery;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 11:19 AM
 */
public interface GroupQuery<Key,U> extends ResultTransformedQuery<U,Grouping<Key,U>,GroupQuery<Key,U>>
{
    GroupQuery<Key,U> groupBy(Exec<U> by);

    GroupQuery<Key,U> groupBy(String by);
}
