package joquery.core.expr;

import joquery.core.QueryException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class ReflectionExpr<T> implements IExpr<T>
{
    private String property;
    private Method method;
    private Field field;

    public ReflectionExpr(String property)
    {
        this.property = property;
    }

    @Override
    public boolean add(IExpr<T> expr)
    {
        return false;
    }

    @Override
    public Object evaluate(T t) throws QueryException
    {
        init(t);
        return getValue(t);
    }

    private void init(T t) throws QueryException
    {
        if (field != null || method != null)
            return;

        try
        {
            initFieldIfNot(t);
        }
        catch (Exception ex)
        {
            try
            {
                initMethodIfNot(t);
            }
            catch (Exception ex1)
            {
                throw new QueryException(String.format("No property or getters can't be found for property %s",property));
            }
        }

        setAccessibleIfNeeded();
    }

    private void setAccessibleIfNeeded()
    {
        if (field != null
                && method == null
                && !field.isAccessible())
        {
            field.setAccessible(true);
        }
        else if (method != null
                && !method.isAccessible())
        {
            method.setAccessible(true);
        }
    }

    private void initFieldIfNot(T t) throws Exception
    {
        field = t.getClass().getField(property);
        if (!field.isAccessible())
            throw new Exception("Field cannot be accessed");
    }

    private void initMethodIfNot(T t) throws Exception
    {
        String camelCase = property.substring(0,1).toUpperCase() + property.substring(1);
        try
        {
            method = t.getClass().getMethod("get" + camelCase);
        }
        catch (NoSuchMethodException e)
        {
            method = t.getClass().getMethod("is" + camelCase);
        }
    }

    private Object getValue(T t) throws QueryException
    {
        try
        {
            if (method != null)
                return method.invoke(t);
            else if (field != null)
                return field.get(t);
            return null;
        }
        catch (Exception ex)
        {
            throw new QueryException(String.format("Unable retrieve value for %s",property));
        }
    }
}
