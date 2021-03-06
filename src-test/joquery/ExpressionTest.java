package joquery;

import assertions.A;
import joquery.core.QueryException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */

public class ExpressionTest
{
    private static Collection<Dto> testList;

    @BeforeClass
    public static void setup()
    {
        testList = new ArrayList<>();
        testList.add(new Dto(1));
        testList.add(new Dto(2));
        testList.add(new Dto(3));
    }

    @AfterClass
    public static void cleanup()
    {
        testList.clear();
        testList = null;
    }

    @Test
    public void whereExpression_Exec_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(s -> s.getId() == 1);

        Collection<Dto> filtered = query.list();
        assertResult(filtered, new int[]{1});
    }

	@Test
	public void whereExpressionEquals_Exec_ShouldFilter() throws QueryException
	{
		Filter<Dto> query = CQ.<Dto>filter()
		                      .from(testList)
		                      .where()
		                      .exec(s -> s.getId() + 1).eq().value(3);

		Collection<Dto> filtered = query.list();
		assertResult(filtered, new int[]{2});
	}

    @Test(expected = QueryException.class)
    public void whereExpression_ExecThrowException_ShouldThrowException() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(s -> {Object o = null; return o.toString();});

        query.list();
    }

    @Test
    public void whereExpression_Property_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .property("id")
                .eq()
                .value(1);

        Collection<Dto> filtered = query.list();
        assertResult(filtered, new int[]{1});
    }

	@Test
	public void whereExpression_PropertyMethodRef_ShouldFilter() throws QueryException
	{
		Filter<Dto> query = CQ.<Dto>filter()
		                      .from(testList)
		                      .where()
		                      .property(Dto::getId)
		                      .eq()
		                      .value(1);

		Collection<Dto> filtered = query.list();
		assertResult(filtered, new int[]{1});
	}

    @Test(expected = QueryException.class)
    public void whereExpression_PropertyWithNoField_ShouldThrowException() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .property("id1")
                .eq()
                .value(1);

        query.list();
    }

    @Test
    public void whereExpression_PropertyOnlyWithField_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .property("id2")
                .eq()
                .value(1);

        Collection<Dto> filtered = query.list();
        assertResult(filtered, new int[]{1});
    }

    @Test
    public void whereExpression_PropertyOnlyWithGetter_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .property("id3")
                .eq()
                .value(1);

        Collection<Dto> filtered = query.list();
        assertResult(filtered, new int[]{1});
    }

    private static void assertResult(Collection<Dto> list,int[] ids)
    {
        A.exp(ids.length).act(list.size(),String.format("Expected items are not retrieved"));
        for (int id : ids)
        {
            boolean found = false;
            for (Dto dto : list)
            {
                found = dto.id == id;
                if (found) break;
            }
            Assert.assertTrue(String.format("Unable to find item with id %s", id), found);
        }
    }

    static class Dto
    {
        private int id;
        private int id2;

        Dto(int id)
        {
            this.id = id;
            this.id2 = id;
        }

        public int getId()
        {
            return id;
        }

        public int getId3()
        {
            return id;
        }
    }
}
