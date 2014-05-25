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
    public void groupById_EmptyList_ShouldReturnEmptyList() throws QueryException
    {
        GroupQuery<Integer,Dto> query = CQ.<Dto,Dto>query()
                .from(new ArrayList<>())
                .<Integer>group()
                .groupBy(Dto::getId);

        Collection<Grouping<Integer,Dto>> groupedList = query.list();
        A.exp(0).act(groupedList.size());
    }

    @Test
    public void groupById_UsingFuncWithValidList_ShouldGroupById() throws QueryException
    {
        GroupQuery<Integer,Dto> query = CQ.<Dto,Dto>query()
                .from(dtoList)
                .<Integer>group()
                .groupBy(Dto::getId)
                .orderBy()
                .property("key");

        Collection<Grouping<Integer,Dto>> groupedList = query.list();
        A.exp(groupById().size()).act(groupedList.size());
        A.exp(createCompareString(groupById())).act(createCompareString(groupedList));
    }

    @Test
    public void groupById_UsingPropertyWithValidList_ShouldGroupById() throws QueryException
    {
        GroupQuery<Integer,Dto> query = CQ.<Dto,Dto>query()
                .from(dtoList)
                .<Integer>group()
                .groupBy("id")
                .orderBy()
                .property("key");

        Collection<Grouping<Integer,Dto>> groupedList = query.list();
        A.exp(groupById().size()).act(groupedList.size());
        A.exp(createCompareString(groupById())).act(createCompareString(groupedList));
    }

    @Test
    public void groupByText_UsingFuncWithValidList_ShouldGroupById() throws QueryException
    {
        GroupQuery<Integer,Dto> query = CQ.<Dto,Dto>query()
                .from(dtoList)
                .<Integer>group()
                .groupBy(Dto::getText)
                .orderBy()
                .property("key");

        Collection<Grouping<Integer,Dto>> groupedList = query.list();
        A.exp(groupByText().size()).act(groupedList.size());
        A.exp(createCompareString(groupByText())).act(createCompareString(groupedList));
    }

    @Test
    public void groupByText_UsingPropertyWithValidList_ShouldGroupById() throws QueryException
    {
        GroupQuery<Integer,Dto> query = CQ.<Dto,Dto>query()
                .from(dtoList)
                .<Integer>group()
                .groupBy("text")
                .orderBy()
                .property("key");

        Collection<Grouping<Integer,Dto>> groupedList = query.list();
        A.exp(groupByText().size()).act(groupedList.size());
        A.exp(createCompareString(groupByText())).act(createCompareString(groupedList));
    }

    @Test
    public void groupByIdAndText_UsingFuncWithValidList_ShouldGroupById() throws QueryException
    {
        GroupQuery<Integer,Dto> query = CQ.<Dto,Dto>query()
                .from(dtoList)
                .<Integer>group()
                .groupBy(Dto::getId)
                .groupBy(Dto::getText);

        Collection<Grouping<Integer,Dto>> groupedList = query.list();
        A.exp(dtoList.size() - 1).act(groupedList.size());
    }

    private <T> String createCompareString(Collection<Grouping<T,Dto>> list)
    {
        StringBuilder sb = new StringBuilder();
        for (Grouping<T, Dto> grouping : list)
        {
            sb.append(grouping.getKey()).append("_{");
            for (Dto dto : grouping.getValues())
            {
                sb.append(dto.getId()).append("-");
                sb.append(dto.getText()).append(",");
            }
            sb.append("}");
        }
        return sb.toString();
    }

    private Collection<Grouping<Integer,Dto>> groupById() throws QueryException
    {
        List<Grouping<Integer,Dto>> groupings = new ArrayList<>();
        for (Dto dto : dtoList)
        {
            Grouping<Integer,Dto> value = CQ.filter(groupings)
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
        sortSingleGroup(groupings);
        return groupings;
    }

    private Collection<Grouping<String,Dto>> groupByText() throws QueryException
    {
        List<Grouping<String,Dto>> groupings = new ArrayList<>();
        for (Dto dto : dtoList)
        {
            Grouping<String,Dto> value = CQ.filter(groupings)
                    .where().property("key").eq().value(dto.getText()).first();

            if (value != null)
            {
                value.Add(dto);
            }
            else
            {
                value = new Grouping<>(dto.getText());
                value.Add(dto);
                groupings.add(value);
            }
        }
        sortSingleGroup(groupings);
        return groupings;
    }

    private <T extends Comparable<T>> void sortSingleGroup(List<Grouping<T, Dto>> groupings)
    {
        Collections.sort(groupings, (o1, o2) -> {
            if (o1.getKey() == null)
                return -1;
            if (o2.getKey() == null)
                return 1;
            return o1.getKey().compareTo(o2.getKey());
        });
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
