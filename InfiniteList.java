import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

interface InfiniteList<T> {
    Optional<T> get();

    static <T> InfiniteList<T> generate(Supplier<? extends T> supplier) {
        return InfiniteListImpl.generate(supplier);
    }

    static <T> InfiniteList<T> iterate(T seed, UnaryOperator<T> f) {
        return InfiniteListImpl.iterate(seed, f);
    }
}