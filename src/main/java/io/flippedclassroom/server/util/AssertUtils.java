package io.flippedclassroom.server.util;

import io.flippedclassroom.server.exception.AssertException;

import java.io.File;
import java.util.function.Supplier;

/**
 * 通用断言
 */
public class AssertUtils {


    public static void assertNotNull(Throwable throwable, Object... objects) throws Throwable {
        for (Object object : objects) {
            assertNotNUllElseThrow(object, () -> throwable);
        }
    }

    public static <X extends Throwable> void assertNotNUllElseThrow(Object object, Supplier<? extends X> exceptionSupplier) throws X {
        if (object == null)
            throw exceptionSupplier.get();
    }

    public static <X extends Throwable> void assertEqualsElseThrow(Object object1, Object object2, Supplier<? extends X> exceptionSupplier) throws X {
        assertNotNUllElseThrow(object1, exceptionSupplier);
        assertNotNUllElseThrow(object2, exceptionSupplier);
        if (!object1.equals(object2))
            throw exceptionSupplier.get();
    }


    public static <X extends Throwable> void assertTrueElseThrow(boolean exp, Supplier<? extends X> exceptionSupplier) throws X {
        if (!exp)
            throw exceptionSupplier.get();
    }


    public static <X extends Throwable> void assertFalseElseThrow(boolean exp, Supplier<? extends X> exceptionSupplier) throws X {
        if (exp)
            throw exceptionSupplier.get();
    }

    public static void assertPositionValid(String position) throws AssertException {
        if (position == null || position.length() == 0) {
            throw new AssertException();
        }
        File file = new File(position);
        if (!file.exists()) {
            throw new AssertException();
        }
    }

    public static <X extends Throwable> void assertPositionValidElseThrow(String position, Supplier<? extends X> exceptionSupplier) throws X {
        if (position == null || position.length() == 0) {
            throw exceptionSupplier.get();
        }
        File file = new File(position);
        if (!file.exists()) {
            throw exceptionSupplier.get();
        }
    }
}
