package general;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.stream.IntStream;

public class General {
  public static final class IterableWrapper implements Iterable<Integer>{
    final PrimitiveIterator.OfInt it;
    IterableWrapper(PrimitiveIterator.OfInt it){this.it=it;}
    @Override public Iterator<Integer> iterator() {return it;}
    }
  public static IterableWrapper range(int endExclusive) {return range(0,endExclusive);}
  public static IterableWrapper range(int startInclusive,int endExclusive) {
    return new IterableWrapper(IntStream.range(startInclusive,endExclusive).iterator());
    }

}
