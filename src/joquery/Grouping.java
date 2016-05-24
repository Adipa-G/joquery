package joquery;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: Adipa
 * Date: 11/10/12
 * Time: 4:50 PM
 */
public class Grouping<T,U>
{
    private T key;
    private Collection<U> values;

    public Grouping(T key)
    {
        this.key = key;
        values = new ArrayList<>();
    }

    public void Add(U u)
    {
        values.add(u);
    }

    public T getKey()
    {
        return key;
    }

    public Collection<U> getValues()
    {
        return values;
    }
}
