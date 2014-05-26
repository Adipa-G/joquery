package joquery.core.collection.expr.condition.combine;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 12:58 PM
 */
public class AndConditionalCombinationExpr<T> extends BaseConditionalCombinationExpr<T>
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
