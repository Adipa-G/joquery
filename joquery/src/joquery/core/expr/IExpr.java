package joquery.core.expr;

import joquery.core.QueryException;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public interface IExpr<T>
{
    boolean add(IExpr<T> expr);

    Object evaluate(T t) throws QueryException;
}
