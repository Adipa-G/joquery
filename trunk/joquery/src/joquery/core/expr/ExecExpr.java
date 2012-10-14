package joquery.core.expr;

import joquery.Exec;
import joquery.core.QueryException;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class ExecExpr<T> implements IExpr<T>
{
    private Exec<T> exec;

    public ExecExpr(Exec<T> exec)
    {
        this.exec = exec;
    }

    @Override
    public boolean add(IExpr<T> expr)
    {
        return false;
    }

    @Override
    public Object evaluate(T t) throws QueryException
    {
        try
        {
            return exec.exec(t);
        }
        catch (Exception ex)
        {
            throw new QueryException(String.format("Exception while evaluating exec %s",exec),ex);
        }
    }
}
