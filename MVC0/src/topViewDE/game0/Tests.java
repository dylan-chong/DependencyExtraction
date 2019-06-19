package topViewDE.game0;

public class Tests {

}
class C{}
class CV<A>{}
interface I1{ default void m(CV<C> v) {} }
interface I2<V>{void m(V v);}
class Mix implements I1,I2<CV<C>>{
  public void m(CV<C>v) {I1.super.m(v);}
}