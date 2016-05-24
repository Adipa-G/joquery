package joquery.core.collection.expr.condition;

import joquery.core.QueryException;
import joquery.core.collection.expr.IExpr;


/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class BetweenConditionalExpr<T> extends ConditionalExpr<T>
{
    protected IExpr<T> right2;

    @Override
    public boolean add(IExpr<T> expr)
    {
        boolean result = super.add(expr);
        if (!result && right2 == null)
        {
            right2 = expr;
            return true;
        }
        return result;
    }

    @Override
    protected void validate() throws QueryException
    {
        super.validate();
        if (right2 == null)
            throw new QueryException(String.format("Additional right segment of %s is null",this));
    }

    @Override
    public Object evaluate(T t) throws QueryException
    {
        validate();
        GeConditionalExpr<T> geExpr = new GeConditionalExpr<>();
        geExpr.left = left;
        geExpr.right = right;

        LeConditionalExpr<T> leExpr = new LeConditionalExpr<>();
        leExpr.left = left;
        leExpr.right = right2;

        boolean ge = (Boolean)geExpr.evaluate(t);
        boolean le = (Boolean)leExpr.evaluate(t);

        return ge && le;
    }
}
