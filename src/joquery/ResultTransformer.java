package joquery;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 10:49 AM
 */
public interface ResultTransformer<T,U>
{
    U transform(T selection);
}
