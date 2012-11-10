package joquery;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 11:19 AM
 */
public interface JoinQuery<T,U,V> extends ResultTransformedQuery<JoinPair<T,U>,V,JoinQuery<T,U,V>>
{
    JoinQuery<T,U,V> on(Exec<T> left,Exec<U> right);

    JoinQuery<T,U,V> on(String left,String right);
}
