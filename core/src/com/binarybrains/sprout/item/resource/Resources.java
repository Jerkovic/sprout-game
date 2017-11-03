package com.binarybrains.sprout.item.resource;


public class Resources {

    public static Resource coal = new Resource("Coal", "Combustible black or dark brown rock consisting \nmainly of carbonized plant matter, found mainly in underground \ndeposits and widely used as fuel", 9);

    public static Resource wood = new Resource("Wood", "Hard fibrous material that forms the main substance \nof the trunk or branches of a tree or shrub.", 8);
    public static Resource stick = new Resource("Stick", "A stick made of wood", 11);
    public static Resource stone = new Resource("Stone", "Hard, solid, nonmetallic mineral matter especially \ngood as building material.", 8);

    public static Resource copperOre = new Resource("Copper Ore", " Rock with a brassy color", 15);
    public static Resource copperBar = new Resource("Copper Bar", "Solid bar of of copper", 60);

    public static Resource ironOre = new Resource("Iron Ore", "Rock from which iron can be profitably extracted.", 40);
    public static Resource ironBar = new Resource("Iron Bar", "Strong, hard magnetic silvery-gray metal", 120);

    public static Resource goldNugget = new Resource("Gold Nugget", "Small lump of gold found ready-formed in the earth.", 80);
    public static Resource goldIngot = new Resource("Gold Bar", "Solid block of gold in a oblong shape.", 240);

    public static Resource diamond = new Resource("Diamond", "A very valuable gem.", 15850);

    public static Resource acorn = new Resource("Acorn", "Fruit of the oak, a smooth oval nut in a rough cuplike base.", 1);

    public static Resource wool = new Resource("Wool", "Wool is the textile fiber obtained from sheep.", 40);
    public static Resource cloth = new Resource("Cloth", "Textile obtained from whool", 200);
    public static Resource string = new Resource("String", "A nice strong string.", 100);

    public static Resource woodFence = new PlantableResource("Wood Fence", "Keep animals contained and the wolf away", 100);
    public static Resource bomb = new PlantableResource("Bomb", "Generates an explosion. Be careful!", 275);
    public static Resource ladder = new PlantableResource("Ladder", "ladder to climb", 80);

    public static Resource salmon = new FoodResource("Salmon", "Nice meatfish, grill it!", 100);
    public static Resource potato = new FoodResource("Potato", "Starchy, tuberous crop.", 6);
    public static Resource apple = new FoodResource("Apple", "A fruit", 5);
    public static Resource banana = new FoodResource("Banana", "An edible fruit, botanically a berry", 8);
    public static Resource orange = new FoodResource("Orange", "A citrus fruit.", 9);
    public static Resource coconut = new FoodResource("Coconut", "A fruit with a hard shell", 13);
    public static Resource chuckBerry = new FoodResource("Chuck Berry", "A very rare berry", 14);
    public static Resource cider = new FoodResource("Cider", "A very refreshing drink made of apples. Consume with care.", 22);
    public static Resource cocktail = new FoodResource("Cocktail", "The trinity cocktail will heal you.", 30);

    public static Resource seeds = new SeedResource("Seed Bag", "A bag of mixed seeds", 21);

}
