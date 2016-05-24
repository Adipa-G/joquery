package joquery.core.collection.expr;

import joquery.core.QueryException;
import joquery.core.QueryMode;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public interface IExpr<T>
{
    boolean supportsMode(QueryMode mode);

    boolean add(IExpr<T> expr);

    Object evaluate(T t) throws QueryException;
}
