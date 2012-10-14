package joquery.core.expr.cmp;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 12:58 PM
 */
public class AndCndExpr<T> extends BaseCombineCndExpr<T>
{
    @Override
    public boolean combine(boolean leftVal, boolean rightVal)
    {
        return leftVal && rightVal;
    }

    @Override
    public String getConditionName()
    {
        return "And";
    }
}
