package com.lx.camera2;

import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Object o = new Object();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        synchronized (o) {
            System.out.println("-------------------------");
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
        System.out.println(System.currentTimeMillis());
    }
}