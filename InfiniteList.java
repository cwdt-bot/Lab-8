import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

interface InfiniteList<T> {
    //level 1
    public Optional<T> get();

    public static <T> InfiniteList<T> generate(Supplier<? extends T> supplier) {
        return InfiniteListImpl.generate(supplier);
    }

    public static <T> InfiniteList<T> iterate(T seed, UnaryOperator<T> f) {
        return InfiniteListImpl.iterate(seed, f);
    }
    //level 2
    public InfiniteList<T> limit(long maxSize);
    public void forEach(Consumer<? super T> action);
    public Object[] toArray();

    //level 3
    public <S> InfiniteList<S> map(Function<? super T, ? extends S> mapper);
    public InfiniteList<T> filter(Predicate<? super T> predicate);
    public InfiniteList<T> takeWhile(Predicate<? super T> predicate);
    
    //level 4
    public long count();
    public Optional<T> reduce(BinaryOperator<T> accumulator);
    public T reduce(T identity, BinaryOperator<T> accumulator);

}