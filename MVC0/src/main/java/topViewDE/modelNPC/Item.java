package topViewDE.modelNPC;

public interface Item{
  public static final Item air=new Item() {};
  public static final Item ground=new Full() {};
  public static final Item grass=new Full() {};
  public static final Item rock=new Full() {};
  public static final Item treeTop=new Tree() {};
  public static final Item treeTrunk=new Tree() {};
  public static final Item water=new Item() {};
  interface Tree extends Item{}
  interface Full extends Item{}
}