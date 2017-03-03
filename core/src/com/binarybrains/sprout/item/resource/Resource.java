package com.binarybrains.sprout.item.resource;


public class Resource {

    public static Resource coal = new Resource("Coal", "Combustible black or dark brown rock consisting \nmainly of carbonized plant matter, found mainly in underground \ndeposits and widely used as fuel");

    public static Resource wood = new Resource("Wood", "Hard fibrous material that forms the main substance \nof the trunk or branches of a tree or shrub.");
    public static Resource stone = new Resource("Stone", "Hard, solid, nonmetallic mineral matter especially \ngood as building material.");

    public static Resource ironOre = new Resource("Iron Ore", "Rock from which iron can be profitably extracted.");
    public static Resource ironBar = new Resource("Iron Bar", "Strong, hard magnetic silvery-gray metall");

    public static Resource goldNugget = new Resource("Gold Nugget", "Small lump of gold found ready-formed in the earth.");
    public static Resource goldIngot = new Resource("Gold Bar", "Solid block of gold in a oblong shape.");

    public static Resource acorn = new Resource("Acorn", "Fruit of the oak, a smooth oval nut in a rough cuplike base.");

    public static Resource woodFence = new PlantableResource("Wood Fence", "Keep animals contained and the wolf away");
    public static Resource bomb = new PlantableResource("Bomb", "Generates an explosion");

    public static Resource salmon = new FoodResource("Salmon", "Nice meatfish, grill it!");
    public static Resource potato = new FoodResource("Potato", "Starchy, tuberous crop.");

    public static Resource seeds = new SeedResource("Seed Bag", "A bag of mixed seeds");


    public final String name;
    public final String description;

    public Resource(String name, String description) {
        if (name.length() > 12) throw new RuntimeException("Name cannot be longer than 12 characters!");
        this.name = name;
        this.description = description;
    }

}