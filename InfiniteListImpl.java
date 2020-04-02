import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

abstract class InfiniteListImpl<T> implements InfiniteList<T> {
    abstract public Optional<T> get();

    public static <T> InfiniteList<T> generate(Supplier<? extends T> supplier) {
        return new InfiniteListImpl<T>(){
        
            @Override
            public Optional<T> get() {
                return Optional.of(supplier.get());
            }
        };
    }

    public static <T> InfiniteList<T> iterate(T seed, UnaryOperator<T> f) {
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

    public InfiniteList<T> limit (long n) throws IllegalArgumentException {
        if (n < 0 ) {
            throw new IllegalArgumentException(""+ n);
        } else {
            return new InfiniteListImpl<T>(){
                long counter = n;

                @Override
                public Optional<T> get() {
                    if (counter > 0) {
                        counter--;
                        return InfiniteListImpl.this.get();
                    } else {
                        return Optional.empty();
                    }
                }
            };
        }
    }

    public void forEach(Consumer<? super T> action) {
        Optional<T> curr;
        while ((curr = this.get()).isPresent()) {
            curr.ifPresent(action);
        }
    }

    public Object[] toArray() {
        ArrayList<T> list = new ArrayList<>();
        this.forEach(x->list.add(x));
        return list.toArray();
    }

    public <S> InfiniteList<S> map(Function<? super T, ? extends S> mapper) {
        return new InfiniteListImpl<S>(){
                
            @Override
            public Optional<S> get() {
                Optional<T> curr = InfiniteListImpl.this.get();
                return curr.map(mapper);                
            }
        };
    }
    /**
     * the only time an empty optional is returned is when an empty optional is passed in
     * values that fail the predicate will not be returned as empty optionals, but call the method again.
     */
    public InfiniteList<T> filter(Predicate<? super T> predicate) {
        return new InfiniteListImpl<T>() {
            
            @Override
            public Optional<T> get() {
                Optional<T> curr = InfiniteListImpl.this.get();
                if (curr.isEmpty() || predicate.test(curr.get())) {
                    return curr;
                } else {
                    return this.get();
                }
                
            }
        };
    }

    public InfiniteList<T> takeWhile(Predicate<? super T> predicate) {
        return new InfiniteListImpl<T>() {
            
            @Override
            public Optional<T> get() {
                Optional<T> curr = InfiniteListImpl.this.get();
                if (curr.isEmpty() || !predicate.test(curr.get())) {
                    return Optional.empty();
                } else {
                    return curr;
                }
                
            }
        };
    }

    public long count() {
        return this.toArray().length;
    }

    public Optional<T> reduce(BinaryOperator<T> accumulator) {
        Optional<T> curr = this.get();
        Optional<T> next = this.get(); 
        if (curr.isEmpty()) {
            return Optional.empty();
        }
        while (next.isPresent()) {
            curr = Optional.of(accumulator.apply(curr.get(), next.get()));
            next = this.get();
        }
        return curr;
    }

    public T reduce(T identity, BinaryOperator<T> accumulator) {
        Optional<T> curr;
        while ((curr = this.get()).isPresent()) {
            identity = accumulator.apply(identity, curr.get());
        }
        return identity;
    }
}