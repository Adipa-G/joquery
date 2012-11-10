package joquery;

/**
 * User: Adipa
 * Date: 10/28/12
 * Time: 1:33 PM
 */
public class JoinPair<T,U>
{
    private T left;
    private U right;

    public JoinPair(T left, U right)
    {
        this.left = left;
        this.right = right;
    }

    public T getLeft()
    {
        return left;
    }

    public U getRight()
    {
        return right;
    }
}
