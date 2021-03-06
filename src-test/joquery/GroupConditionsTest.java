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

public class GroupConditionsTest
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
    public void groupConditionById_EmptyList_ShouldReturnEmptyList() throws QueryException
    {
        GroupQuery<Integer,Dto> query = CQ.<Dto,Dto>query()
                .from(new ArrayList<Dto>())
                .<Integer>group()
                .groupBy(Dto::getId)
                .where()
                .exec(Grouping::getKey).eq().value(1);

        Collection<Grouping<Integer,Dto>> groupedList = query.list();
        A.exp(0).act(groupedList.size());
    }

    @Test
    public void groupConditionById_UsingExecWithValidList_ShouldFilterById() throws QueryException
    {
        GroupQuery<Integer,Dto> query = CQ.<Dto,Dto>query()
                .from(dtoList)
                .<Integer>group()
                .groupBy(Dto::getId)
                .where()
                .exec(Grouping::getKey).eq().value(1);

        Collection<Grouping<Integer,Dto>> groupedList = query.list();
        A.exp(1).act(groupedList.size());
        A.exp(createCompareString(groupById(1))).act(createCompareString(groupedList));
    }

    @Test
    public void groupConditionById_UsingPropertyWithValidList_ShouldFilterById() throws QueryException
    {
        GroupQuery<Integer,Dto> query = CQ.<Dto,Dto>query()
                .from(dtoList)
                .<Integer>group()
                .groupBy(Dto::getId)
                .where()
                .property("key").eq().value(1);

        Collection<Grouping<Integer,Dto>> groupedList = query.list();
        A.exp(1).act(groupedList.size());
        A.exp(createCompareString(groupById(1))).act(createCompareString(groupedList));
    }

    @Test
    public void groupConditionByText_UsingExecWithValidList_ShouldFilterByText() throws QueryException
    {
        GroupQuery<String,Dto> query = CQ.<Dto,Dto>query()
                .from(dtoList)
                .<String>group()
                .groupBy("text")
                .where()
                .exec(Grouping::getKey).eq().value("A");

        Collection<Grouping<String,Dto>> groupedList = query.list();
        A.exp(groupByText("A").size()).act(groupedList.size());
        A.exp(createCompareString(groupByText("A"))).act(createCompareString(groupedList));
    }

    @Test
    public void groupConditionByText_UsingPropertyWithValidList_ShouldFilterByText() throws QueryException
    {
        GroupQuery<String,Dto> query = CQ.<Dto,Dto>query()
                .from(dtoList)
                .<String>group()
                .groupBy("text")
                .where()
                .property("key").eq().value("A");

        Collection<Grouping<String,Dto>> groupedList = query.list();
        A.exp(groupByText("A").size()).act(groupedList.size());
        A.exp(createCompareString(groupByText("A"))).act(createCompareString(groupedList));
    }

    @Test
    public void groupConditionByIdAndText_UsingExecWithValidList_ShouldFilterByIdAndText() throws QueryException
    {
        GroupQuery<Object[],Dto> query = CQ.<Dto,Dto>query()
                .from(dtoList)
                .<Object[]>group()
                .groupBy("id")
                .groupBy("text")
                .where()
                .exec(dtoGrouping -> dtoGrouping.getKey()[0].equals(1)
                                     && dtoGrouping.getKey()[1].equals("A"));

        Collection<Grouping<Object[],Dto>> groupedList = query.list();
        A.exp(1).act(groupedList.size());
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

    private Collection<Grouping<Integer,Dto>> groupById(int id) throws QueryException
    {
        List<Grouping<Integer,Dto>> groupings = new ArrayList<>();
        for (Dto dto : dtoList)
        {
            if (dto.getId() != id)
            {
                continue;
            }

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
        sortSingleGroup(groupings);
        return groupings;
    }

    private Collection<Grouping<String,Dto>> groupByText(String text) throws QueryException
    {
        List<Grouping<String,Dto>> groupings = new ArrayList<>();
        for (Dto dto : dtoList)
        {
            if (dto.getText() == null || !dto.getText().equals(text))
            {
                continue;
            }

            Grouping<String,Dto> value = CQ.<Grouping<String,Dto>>filter(groupings)
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
        Collections.sort(groupings, new Comparator<Grouping<T, Dto>>()
        {
            @Override
            public int compare(Grouping<T, Dto> o1, Grouping<T, Dto> o2)
            {
                if (o1.getKey() == null)
                    return -1;
                if (o2.getKey() == null)
                    return 1;
                return o1.getKey().compareTo(o2.getKey());
            }
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
