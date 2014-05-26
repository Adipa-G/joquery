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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JoinPair joinPair = (JoinPair) o;

        if (left != null ? !left.equals(joinPair.left) : joinPair.left != null) return false;
        if (right != null ? !right.equals(joinPair.right) : joinPair.right != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }
}
