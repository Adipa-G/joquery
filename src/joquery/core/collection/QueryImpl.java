package joquery.core.collection;

import joquery.Query;
import joquery.ResultTransformer;
import joquery.core.QueryException;
import joquery.core.QueryMode;
import joquery.core.collection.expr.*;
import joquery.core.collection.expr.condition.*;
import joquery.core.collection.expr.condition.combine.AndConditionalCombinationExpr;
import joquery.core.collection.expr.condition.combine.OrConditionalCombinationExpr;

import java.util.*;
import java.util.function.Function;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public abstract class QueryImpl<T,W extends Query<T,W>> implements Query<T, W>
{
    private QueryMode queryMode;

    private IExpr<T> defaultSelection;

    private List<IExpr<T>> selections;
    private List<IExpr<T>> sortExpressions;
    private Iterable<T> items;
    private IExpr<T> condition;

    protected QueryImpl()
    {
        setItems(Collections.<T>emptyList());
        selections = new ArrayList<>();
        sortExpressions = new ArrayList<>();
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

    public List<IExpr<T>> getSortExpressions()
    {
        return sortExpressions;
    }

    private boolean addSortExpression(IExpr<T> expr)
    {
        return this.sortExpressions.add(expr);
    }

    protected <X> X first(Collection<X> xs)
    {
        if (xs.iterator().hasNext())
            return xs.iterator().next();
        return null;
    }

    protected <X> X last(Collection<X> xs)
    {
        Iterator<X> iterator = xs.iterator();
        X last = null;
        while (iterator.hasNext())
        {
            last = iterator.next();
        }
        return last;
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
    public W orderBy() throws QueryException
    {
        queryMode = QueryMode.SORT;
        //noinspection unchecked
        return (W)this;
    }

	@Override
	public W exec(Function<T,?> function) throws QueryException
	{
		return addExpr(new FunctionExpr<>(function));
	}

    @Override
    public W property(String property) throws QueryException
    {
        return addExpr(new ReflectionExpr<>(property));
    }

	@Override
	public W property(Function<T, ?> property) throws QueryException
	{
		return addExpr(new FunctionExpr<>(property));
	}

	@Override
    public W value(Object value) throws QueryException
    {
        return addExpr(new ConstExpr<>(value));
    }

    @Override
    public W and() throws QueryException
    {
        return addExpr(new AndConditionalCombinationExpr<>());
    }

    @Override
    public W or() throws QueryException
    {
        return addExpr(new OrConditionalCombinationExpr<>());
    }

    @Override
    public W eq() throws QueryException
    {
        return addExpr(new EqConditionalExpr<>());
    }

    @Override
    public W lt() throws QueryException
    {
        return addExpr(new LtConditionalExpr<>());
    }

    @Override
    public W le() throws QueryException
    {
        return addExpr(new LeConditionalExpr<>());
    }

    @Override
    public W gt() throws QueryException
    {
        return addExpr(new GtConditionalExpr<>());
    }

    @Override
    public W ge() throws QueryException
    {
        return addExpr(new GeConditionalExpr<>());
    }

    @Override
    public W in() throws QueryException
    {
        return addExpr(new InConditionalExpr<>());
    }

    @Override
    public W between() throws QueryException
    {
        return addExpr(new BetweenConditionalExpr<>());
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
            case SORT:
                added = addSortExpr(expr);
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

    private boolean addSortExpr(IExpr<T> expr)
    {
        return expr.supportsMode(queryMode) && addSortExpression(expr);
    }

    protected  <U> Collection<U> transformDefaultSelection() throws QueryException
    {
        ResultTransformer<T,U> transformer = createDefaultTransformer();
        return transformDefaultSelection(transformer);
    }

    protected <U> Collection<U> transformDefaultSelection(ResultTransformer<T,U> transformer) throws QueryException
    {
        Collection<U> retVal = new ArrayList<>();
        Collection<T> selectionResult = filterSelectAndSort();

        for (T result : selectionResult)
        {
            retVal.add(transformer.transform(result));
        }
        return retVal;
    }

    private <U> ResultTransformer<T,U> createDefaultTransformer()
    {
	    //noinspection unchecked
        return selection -> (U) selection;
    }

    protected <U> Collection<U> transformCustomSelection(ResultTransformer<Object[], U> transformer) throws QueryException
    {
        Collection<U> retVal = new ArrayList<>();
        Collection<Object[]> selectionResult = filterSelectAndSort();

        for (Object[] result : selectionResult)
        {
            retVal.add(transformer.transform(result));
        }
        return retVal;
    }

    private <U> Collection<U> filterSelectAndSort() throws QueryException
    {
        Collection<T> filtered = filterByConditions();
        Collection<T> sorted = sortCollection(filtered);
        return selectFromList(sorted);
    }

    private Collection<T> filterByConditions() throws QueryException
    {
        Collection<T> filteredList = new ArrayList<>();
        for (T t : getItems())
        {
            Object result = condition != null ? condition.evaluate(t) : true;
            if (result instanceof Boolean)
            {
                boolean boolResult = (Boolean)result;
                if (boolResult)
                {
                    filteredList.add(t);
                }
            }
            else
            {
                throw new QueryException(String.format("Evaluation resulted in unexpected result %s should have been a boolean",result));
            }
        }
        return filteredList;
    }

    private Collection<T> sortCollection(Collection<T> collectionToSort) throws QueryException
    {
        Comparator<T> comparator = (t1, t2) -> {
            int compareResult = 0;
            for (IExpr<T> expr : getSortExpressions())
            {
                Object val1 = EvaluateExpression(t1, expr);
                Object val2 = EvaluateExpression(t2, expr);

                if (val1 == val2)
                {
                    compareResult = 0;
                }
                else if (val1 == null)
                {
                    compareResult = -1;
                }
                else if (val2 == null)
                {
                    compareResult = 1;
                }
                else if (val1 instanceof Comparable
                        && val2 instanceof Comparable)
                {
                    Comparable val1Comparable = (Comparable) val1;
                    //noinspection unchecked
                    compareResult = val1Comparable.compareTo(val2);
                }

                if (compareResult != 0)
                    break;
            }

            return compareResult;
        };

        List<T> sortedList = new ArrayList<>(collectionToSort);
        Collections.sort(sortedList,comparator);
        return sortedList;
    }

    private Object EvaluateExpression(T t, IExpr<T> expr)
    {
        Object result = null;
        try
        {
            result = expr.evaluate(t);
        }
        catch (QueryException ignored)
        {}
        return result;
    }

    private <U> Collection<U> selectFromList(Collection<T> listToSelect) throws QueryException
    {
        Collection<U> selectedList = new ArrayList<>();
        for (T t : listToSelect)
        {
            U selection = selectFromItem(t);
            selectedList.add(selection);
        }
        return selectedList;
    }

    private <U> U selectFromItem(T t) throws QueryException
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
	        defaultSelection = new FunctionExpr<>(s -> s);
        }
    }


}
