package assertions;

import org.junit.Assert;

import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/28/12
 * Time: 2:06 PM
 */
public class A<T>
{
    private T expected;

    public A(T expected)
    {
        this.expected = expected;
    }

    public void act(T actual)
    {
        compare(expected,actual,"");
    }

    public void act(T actual,String message)
    {
        compare(expected,actual,message);
    }

    private void compare(T exp,T act,String message)
    {
        if (exp instanceof Collection)
        {
            Collection expCollection = (Collection) exp;
            Collection actCollection = (Collection) act;
            compareCollection(expCollection,actCollection,message);
        }
        else
        {
            Assert.assertEquals(message,exp,act);
        }
    }

    private <U extends Collection> void compareCollection(U exp,U act,String message)
    {
        Assert.assertEquals(message + " -> Collection sizes does not match",exp.size(),act.size());

        for (Object e : exp)
        {
            boolean found = false;
            for (Object a : act)
            {
                if (e.equals(a))
                {
                    found = true;
                    break;
                }
            }
            if (!found)
                Assert.fail(String.format(message + " -> expected object %s does not exists in actual object list",e));
        }
    }

    public static <T> A<T> exp(T t)
    {
        return new A<>(t);
    }
}
