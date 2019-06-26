package MVC0;

public class GenericsTest {
}

interface Example1 {
  class C {
  }

  class CV<A> {
  }

  interface I1 {
    default void m(CV<C> v) {
    }
  }

  interface I2<V> {
    void m(V v);
  }

  class Mix implements I1, I2<CV<C>> {
//  @Override public void m(CV<C> v) { I1.super.m(v); }
  }
}

interface Example2 {

  interface II1 {
    default int m(int x) {
      return 1;
    }
  }

  interface II2 {
    int m(int x);
  }

  class CC implements II1, II2 {
//  @Override
//  public int m(int x) {
//    return 0;
//  }
  }
}

interface Example3 {
  interface II1 {
    int m(int x);
  }

  interface II2 {
    default int m(int x) {
      return 1;
    }
  }

  class CC implements II1, II2 {
//  @Override
//  public int m(int x) {
//    return 0;
//  }
  }
}

interface Example4 {
  interface II1 {
    default int m(int x) {
      return 1;
    }
  }

  interface II2 {
    default int m(int x) {
      return 1;
    }
  }

  class CC implements II1, II2 {
//  @Override
//  public int m(int x) {
//    return 0;
//  }
  }
}

/*
> java GenericsTest.java
GenericsTest.java:20: error: Mix is not abstract and does not override abstract method m(CV<C>) in I2
  class Mix implements I1, I2<CV<C>> {
  ^
GenericsTest.java:37: error: CC is not abstract and does not override abstract method m(int) in II2
  class CC implements II1, II2 {
  ^
GenericsTest.java:56: error: CC is not abstract and does not override abstract method m(int) in II1
  class CC implements II1, II2 {
  ^
GenericsTest.java:77: error: types II1 and II2 are incompatible;
  class CC implements II1, II2 {
  ^
  class CC inherits unrelated defaults for m(int) from types II1 and II2
4 errors
error: compilation failed
 */

/*
> ./gradlew clean test run

> Task :compileJava FAILED
/Users/Dylan/Dropbox/School/engr489/MarcoDependencyExtraction/MVC0/src/main/java/MVC0/GenericsTest.java:22: error: Mix is not abstract and does not override abstract method m(CV<C>) in I2
  class Mix implements I1, I2<CV<C>> {
  ^
/Users/Dylan/Dropbox/School/engr489/MarcoDependencyExtraction/MVC0/src/main/java/MVC0/GenericsTest.java:39: error: CC is not abstract and does not override abstract method m(int) in II2
  class CC implements II1, II2 {
  ^
/Users/Dylan/Dropbox/School/engr489/MarcoDependencyExtraction/MVC0/src/main/java/MVC0/GenericsTest.java:58: error: CC is not abstract and does not override abstract method m(int) in II1
  class CC implements II1, II2 {
  ^
/Users/Dylan/Dropbox/School/engr489/MarcoDependencyExtraction/MVC0/src/main/java/MVC0/GenericsTest.java:79: error: types II1 and II2 are incompatible;
  class CC implements II1, II2 {
  ^
  class CC inherits unrelated defaults for m(int) from types II1 and II2
/Users/Dylan/Dropbox/School/engr489/MarcoDependencyExtraction/MVC0/src/main/java/topViewDE/modelNPC/Main.java:17: error: Game is not abstract and does not override abstract method drawCell(Viewport<ModelMap,Drawable>,int,int,int) in View
class Game extends JFrame implements
^
/Users/Dylan/Dropbox/School/engr489/MarcoDependencyExtraction/MVC0/src/main/java/topViewDE/modelHeightMap/Main.java:17: error: Game is not abstract and does not override abstract method drawCell(Viewport<ModelMap,Drawable>,int,int,int) in View
class Game extends JFrame implements
^
/Users/Dylan/Dropbox/School/engr489/MarcoDependencyExtraction/MVC0/src/main/java/topViewDE/game0/Game.java:17: error: Game is not abstract and does not override abstract method drawCell(Viewport<ModelMap,Drawable>,int,int,int) in View
public class Game extends JFrame implements
       ^
7 errors

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':compileJava'.
> Compilation failed; see the compiler error output for details.

* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output. Run with --scan to get full insights.

* Get more help at https://help.gradle.org

BUILD FAILED in 1s
2 actionable tasks: 2 executed
*/
