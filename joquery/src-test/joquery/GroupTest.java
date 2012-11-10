package joquery;

import assertions.A;
import joquery.core.QueryException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */

public class GroupTest
{
    private static Collection<Dto> dtoList;

    @BeforeClass
    public static void setup()
    {
        dtoList = new ArrayList<>();

        dtoList.add(new Dto(1, "A"));
        dtoList.add(new Dto(3, "C"));
        dtoList.add(new Dto(2, "C"));
        dtoList.add(new Dto(4, "B"));
        dtoList.add(new Dto(5, "E"));
        dtoList.add(new Dto(5, "F"));
        dtoList.add(new Dto(5, null));
    }

    @AfterClass
    public static void cleanup()
    {
        dtoList.clear();
        dtoList = null;
    }

    @Test
    public void GroupById_EmptyList_ShouldReturnEmptyList() throws QueryException
    {
        GroupQuery<Integer,Dto> query = CQ.<Dto,Dto>query()
                .from(new ArrayList<Dto>())
                .<Integer>group()
                .groupBy(new Exec<Dto>()
                {
                    @Override
                    public Object exec(Dto dto)
                    {
                        return dto.getId();
                    }
                });

        Collection<Grouping<Integer,Dto>> groupedList = query.list();
        A.exp(0).act(groupedList.size());
    }

    @Test
    public void GroupById_ValidList_ShouldGroupById() throws QueryException
    {
        GroupQuery<Integer,Dto> query = CQ.<Dto,Dto>query()
                .from(dtoList)
                .<Integer>group()
                .groupBy(new Exec<Dto>()
                {
                    @Override
                    public Object exec(Dto dto)
                    {
                        return dto.getId();
                    }
                });

        Collection<Grouping<Integer,Dto>> groupedList = query.list();
        //A.exp(groupById()).act(groupedList);
    }

    private Collection<Grouping<Integer,Dto>> groupById() throws QueryException
    {
        Collection<Grouping<Integer,Dto>> groupings = new ArrayList<>();
        for (Dto dto : dtoList)
        {
            Grouping<Integer,Dto> value = CQ.<Grouping<Integer,Dto>>filter(groupings)
                .where().property("key").eq().value(dto.getId()).first();

            if (value != null)
            {
                value.Add(dto);
            }
            else
            {
                value = new Grouping<>(dto.getId());
                value.Add(dto);
                groupings.add(value);
            }
        }
        return groupings;
    }

    static class Dto
    {
        private int id;
        private String text;

        Dto(int id, String text)
        {
            this.id = id;
            this.text = text;
        }

        public int getId()
        {
            return id;
        }

        public String getText()
        {
            return text;
        }
    }
}
