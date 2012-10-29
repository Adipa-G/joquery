package joquery.core.collection;

import joquery.Exec;
import joquery.Query;
import joquery.ResultTransformer;
import joquery.core.QueryException;
import joquery.core.QueryMode;
import joquery.core.collection.expr.ConstExpr;
import joquery.core.collection.expr.ExecExpr;
import joquery.core.collection.expr.IExpr;
import joquery.core.collection.expr.ReflectionExpr;
import joquery.core.collection.expr.condition.*;
import joquery.core.collection.expr.condition.combine.AndConditionalCombinationExpr;
import joquery.core.collection.expr.condition.combine.OrConditionalCombinationExpr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public abstract class QueryImpl<T,W extends Query> implements Query<T, W>
{
    private QueryMode queryMode;

    private IExpr<T> defaultSelection;

    private List<IExpr<T>> selections;
    private Iterable<T> items;
    private IExpr<T> condition;

    protected QueryImpl()
    {
        setItems(Collections.<T>emptyList());
        selections = new ArrayList<>();
    }

    public Iterable<T> getItems() throws QueryException
    {
        return items;
    }

    public void setItems(Iterable<T> items)
    {
        this.items = items;
    }

    public List<IExpr<T>> getSelections()
    {
        return selections;
    }

    private boolean addSelection(IExpr<T> selection)
    {
        return this.selections.add(selection);
    }

    @Override
    public W from(Iterable<T> list)
    {
        queryMode = QueryMode.FROM;
        setItems(list);
        //noinspection unchecked
        return (W)this;
    }

    @Override
    public W where()
    {
        queryMode = QueryMode.WHERE;
        //noinspection unchecked
        return (W)this;
    }

    @Override
    public W select()
    {
        queryMode = QueryMode.SELECT;
        //noinspection unchecked
        return (W)this;
    }

    @Override
    public W exec(Exec<T> exec) throws QueryException
    {
        return addExpr(new ExecExpr<>(exec));
    }

    @Override
    public W property(String property) throws QueryException
    {
        return addExpr(new ReflectionExpr<T>(property));
    }

    @Override
    public W value(Object value) throws QueryException
    {
        return addExpr(new ConstExpr<T>(value));
    }

    @Override
    public W and() throws QueryException
    {
        return addExpr(new AndConditionalCombinationExpr<T>());
    }

    @Override
    public W or() throws QueryException
    {
        return addExpr(new OrConditionalCombinationExpr<T>());
    }

    @Override
    public W eq() throws QueryException
    {
        return addExpr(new EqConditionalExpr<T>());
    }

    @Override
    public W lt() throws QueryException
    {
        return addExpr(new LtConditionalExpr<T>());
    }

    @Override
    public W le() throws QueryException
    {
        return addExpr(new LeConditionalExpr<T>());
    }

    @Override
    public W gt() throws QueryException
    {
        return addExpr(new GtConditionalExpr<T>());
    }

    @Override
    public W ge() throws QueryException
    {
        return addExpr(new GeConditionalExpr<T>());
    }

    @Override
    public W in() throws QueryException
    {
        return addExpr(new InConditionalExpr<T>());
    }

    @Override
    public W between() throws QueryException
    {
        return addExpr(new BetweenConditionalExpr<T>());
    }

    private W addExpr(IExpr<T> expr) throws QueryException
    {
        boolean added;

        switch (queryMode)
        {
            case SELECT:
                added = addSelectExpr(expr);
                break;
            case FROM:
                throw new QueryException("cannot add expressions while query in from mode " + expr);
            case WHERE:
                added = addWhereExpr(expr);
                break;
            default:
                added = false;
        }

        if (!added)
            throw new QueryException("failed while adding expression to tree " + expr);

        //noinspection unchecked
        return (W)this;
    }

    private boolean addSelectExpr(IExpr<T> expr)
    {
        return expr.supportsMode(queryMode) && addSelection(expr);
    }

    private boolean addWhereExpr(IExpr<T> expr)
    {
        boolean added;

        if (!expr.supportsMode(queryMode))
             return false;

        if (condition != null)
        {
            added = condition.add(expr);
            if (!added)
            {
                added = expr.add(condition);
                condition = expr;
            }
        }
        else
        {
            added = true;
            condition = expr;
        }
        return added;
    }

    protected  <U> Collection<U> transformDefaultSelection() throws QueryException
    {
        ResultTransformer<T,U> transformer = createDefaultTransformer();
        return transformDefaultSelection(transformer);
    }

    protected <U> Collection<U> transformDefaultSelection(ResultTransformer<T,U> transformer) throws QueryException
    {
        Collection<U> retVal = new ArrayList<>();
        Collection<T> selectionResult = executeSelect();

        for (T result : selectionResult)
        {
            retVal.add(transformer.transform(result));
        }
        return retVal;
    }

    private <U> ResultTransformer<T,U> createDefaultTransformer()
    {
        return new ResultTransformer<T,U>()
        {
            @Override
            public U transform(T selection)
            {
                //noinspection unchecked
                return (U) selection;
            }
        };
    }

    protected <U> Collection<U> transformCustomSelection(ResultTransformer<Object[], U> transformer) throws QueryException
    {
        Collection<U> retVal = new ArrayList<>();
        Collection<Object[]> selectionResult = executeSelect();

        for (Object[] result : selectionResult)
        {
            retVal.add(transformer.transform(result));
        }
        return retVal;
    }

    private <U> Collection<U> executeSelect() throws QueryException
    {
        Collection<U> retVal = new ArrayList<>();
        for (T t : getItems())
        {
            Object result = condition != null ? condition.evaluate(t) : true;
            if (result instanceof Boolean)
            {
                boolean boolResult = (Boolean)result;
                if (boolResult)
                {
                    U selection = doSelectionOn(t);
                    retVal.add(selection);
                }
            }
            else
            {
                throw new QueryException(String.format("Evaluation resulted in unexpected result %s should have been a boolean",result));
            }
        }
        return retVal;
    }

    private <U> U doSelectionOn(T t) throws QueryException
    {
        initDefaultSelectionIfNeeded();
        if (getSelections().size() == 0)
        {
            //noinspection unchecked
            return (U)defaultSelection.evaluate(t);
        }
        else
        {
            List<IExpr<T>> selectionList = getSelections();

            Object[] result = new Object[selectionList.size()];
            for (int i = 0; i < selectionList.size(); i++)
            {
                IExpr<T> selection = selectionList.get(i);
                result[i] = selection.evaluate(t);
            }
            //noinspection unchecked
            return (U)result;
        }
    }

    private void initDefaultSelectionIfNeeded()
    {
        if (getSelections().size() == 0
                && defaultSelection == null)
        {
            defaultSelection = new ExecExpr<>(new Exec<T>()
            {
                @Override
                public Object exec(T t)
                {
                    return t;
                }
            });
        }
    }
}
