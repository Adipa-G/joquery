package joquery.core.expr;

import joquery.IExec;
import joquery.core.QueryException;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class ExecExpr<T> implements IExpr<T>
{
    private IExec<T> exec;

    public ExecExpr(IExec<T> exec)
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
