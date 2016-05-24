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

public class SortTest
{
    private static Collection<Dto> unsortedList;

    @BeforeClass
    public static void setup()
    {
        unsortedList = new ArrayList<>();

        unsortedList.add(new Dto(1, "A"));
        unsortedList.add(new Dto(3, "C"));
        unsortedList.add(new Dto(2, "C"));
        unsortedList.add(new Dto(4, "B"));
        unsortedList.add(new Dto(5, "E"));
        unsortedList.add(new Dto(5, "F"));
        unsortedList.add(new Dto(5, null));
    }

    @AfterClass
    public static void cleanup()
    {
        unsortedList.clear();
        unsortedList = null;
    }

    @Test
    public void sort_EmptyList_ShouldReturnEmptyList() throws QueryException
    {
        Filter<Dto> filter = CQ.<Dto>filter()
                .from(new ArrayList<>())
                .orderBy()
                .property(Dto::getId);

        Collection<Dto> sortedList = filter.list();

        A.exp(0).act(sortedList.size());
    }

    @Test
    public void sortWithoutExpression_ValidList_ShouldReturnSameList() throws QueryException
    {
        Filter<Dto> filter = CQ.<Dto>filter()
                .from(unsortedList)
                .orderBy();

        Collection<Dto> sortedList = filter.list();

        A.exp(unsortedList).act(sortedList);
    }

    @Test
    public void sortWithFuncExpression_ValidList_ShouldSort() throws QueryException
    {
        Filter<Dto> filter = CQ.<Dto>filter()
                .from(unsortedList)
                .orderBy()
                .property(Dto::getId);

        Collection<Dto> sortedList = filter.list();

        A.exp(sortById()).act(sortedList);
        A.exp(listOfIds(sortById())).act(listOfIds(sortedList));
    }

    @Test
    public void sortWithPropertyExpression_ValidList_ShouldSort() throws QueryException
    {
        Filter<Dto> filter = CQ.<Dto>filter()
                .from(unsortedList)
                .orderBy()
                .property("id");

        Collection<Dto> sortedList = filter.list();

        A.exp(sortById()).act(sortedList);
        A.exp(listOfIds(sortById())).act(listOfIds(sortedList));
    }

    @Test
    public void sortByTwoFunc_ValidList_ShouldSort() throws QueryException
    {
        Filter<Dto> filter = CQ.<Dto>filter()
                .from(unsortedList)
                .orderBy()
                .property(Dto::getId)
                .property(Dto::getId);

        Collection<Dto> sortedList = filter.list();

        A.exp(sortByIdAndText()).act(sortedList);
        A.exp(listOfIds(sortByIdAndText())).act(listOfIds(sortedList));
    }

    @Test
    public void sortByTwoProperties_ValidList_ShouldSort() throws QueryException
    {
        SelectionQuery<Dto,Dto> filter = CQ.<Dto,Dto>query()
                .from(unsortedList)
                .orderBy()
                .property("id")
                .property("text");

        Collection<Dto> sortedList = filter.list();

        A.exp(sortByIdAndText()).act(sortedList);
        A.exp(listOfIds(sortByIdAndText())).act(listOfIds(sortedList));
    }

    private Collection<Dto> sortById()
    {
        List<Dto> sortedList = new ArrayList<>();
        sortedList.addAll(unsortedList);
        Collections.sort(sortedList, (dto1, dto2) -> {
            Integer id1 = dto1.getId();
            return id1.compareTo(dto2.getId());
        });
        return sortedList;
    }

    private Collection<Dto> sortByIdAndText()
    {
        List<Dto> sortedList = new ArrayList<>();
        sortedList.addAll(unsortedList);
        Collections.sort(sortedList, (dto1, dto2) -> {
            Integer id1 = dto1.getId();
            int value = id1.compareTo(dto2.getId());
            if (value == 0)
            {
                String text1 = dto1.getText();
                if (text1 == null)
                    return dto2.getText() == null ? 0 : -1;
                return text1.compareTo(dto2.getText());
            }
            return value;
        });
        return sortedList;
    }

    private String listOfIds(Collection<Dto> list)
    {
        StringBuilder idListBuilder = new StringBuilder();
        for (Dto dto : list)
        {
            idListBuilder.append(",").append(dto.getId());
        }
        return idListBuilder.toString();
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
