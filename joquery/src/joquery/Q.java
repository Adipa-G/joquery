package joquery;

import joquery.core.SimpleQueryImpl;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 10:52 AM
 */
public class Q
{
    public static <T> SimpleQuery<T> simple()
    {
        return new SimpleQueryImpl<>();
    }
}
