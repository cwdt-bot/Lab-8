import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

abstract class InfiniteListImpl<T> implements InfiniteList<T> {
    abstract public Optional<T> get();

    public static <T> InfiniteListImpl<T> generate(Supplier<? extends T> supplier) {
        return new InfiniteListImpl<T>(){
        
            @Override
            public Optional<T> get() {
                return Optional.of(supplier.get());
            }
        };
    }

    public static <T> InfiniteListImpl<T> iterate(T seed, UnaryOperator<T> f) {
        return new InfiniteListImpl<T>(){
                private boolean first = true;
                private T element = seed;

                @Override
                public Optional<T> get() {
                    if (first) {
                        first = false;
                    } else {
                        element = f.apply(element);
                    }
                    return Optional.of(element);
                }
        };
    }
}