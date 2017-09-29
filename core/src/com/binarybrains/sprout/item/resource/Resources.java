package com.binarybrains.sprout.item.resource;


public class Resources {

    public static Resource coal = new Resource("Coal", "Combustible black or dark brown rock consisting \nmainly of carbonized plant matter, found mainly in underground \ndeposits and widely used as fuel");

    public static Resource wood = new Resource("Wood", "Hard fibrous material that forms the main substance \nof the trunk or branches of a tree or shrub.");
    public static Resource stick = new Resource("Stick", "A stick made of wood");
    public static Resource stone = new Resource("Stone", "Hard, solid, nonmetallic mineral matter especially \ngood as building material.");

    public static Resource copperOre = new Resource("Copper Ore", " Rock with a brassy color");
    public static Resource copperBar = new Resource("Copper Bar", "Solid bar of of copper");

    public static Resource ironOre = new Resource("Iron Ore", "Rock from which iron can be profitably extracted.");
    public static Resource ironBar = new Resource("Iron Bar", "Strong, hard magnetic silvery-gray metal");

    public static Resource goldNugget = new Resource("Gold Nugget", "Small lump of gold found ready-formed in the earth.");
    public static Resource goldIngot = new Resource("Gold Bar", "Solid block of gold in a oblong shape.");

    public static Resource diamond = new Resource("Diamond", "A very valuable gem.");

    public static Resource acorn = new Resource("Acorn", "Fruit of the oak, a smooth oval nut in a rough cuplike base.");

    public static Resource wool = new Resource("Wool", "Wool is the textile fiber obtained from sheep.");
    public static Resource cloth = new Resource("Cloth", "Textile obtained from whool");
    public static Resource string = new Resource("String", "A nice strong string.");

    public static Resource woodFence = new PlantableResource("Wood Fence", "Keep animals contained and the wolf away");
    public static Resource bomb = new PlantableResource("Bomb", "Generates an explosion. Be careful!");
    public static Resource ladder = new PlantableResource("Ladder", "ladder to climb");

    public static Resource salmon = new FoodResource("Salmon", "Nice meatfish, grill it!");
    public static Resource potato = new FoodResource("Potato", "Starchy, tuberous crop.");
    public static Resource apple = new FoodResource("Apple", "A fruit");
    public static Resource banana = new FoodResource("Banana", "A yellow bended fruit");
    public static Resource coconut = new FoodResource("Coconut", "A fruit with a hard shell");
    public static Resource chuckBerry = new FoodResource("Chuck Berry", "A very rare berry");
    public static Resource cider = new FoodResource("Cider", "A very refreshing drink made of apples. Consume with care.");
    public static Resource cocktail = new FoodResource("Cocktail", "The trinity cocktail will heal you.");

    public static Resource seeds = new SeedResource("Seed Bag", "A bag of mixed seeds");

}
