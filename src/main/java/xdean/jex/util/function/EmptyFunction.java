package xdean.jex.util.function;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import xdean.jex.internal.codecov.CodecovIgnore;

@CodecovIgnore
public class EmptyFunction {
  public static Runnable runnable() {
    return () -> {
    };
  }

  public static <T> Consumer<T> consumer() {
    return t -> {
    };
  }

  public static <T> UnaryOperator<T> function() {
    return t -> t;
  }

  public static <T> Supplier<T> supplier() {
    return () -> null;
  }

  public static <K, T> BiConsumer<K, T> biconsumer() {
    return (k, t) -> {
    };
  }
}
