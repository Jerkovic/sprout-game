package com.binarybrains.sprout.level;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.house.Door;
import com.binarybrains.sprout.entity.npc.Npc;
import com.binarybrains.sprout.level.pathfind.Astar;
import com.binarybrains.sprout.level.renderer.LevelMapRenderer;
import com.binarybrains.sprout.level.tile.*;
import com.binarybrains.sprout.locations.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class LevelEngine extends Stage {

    public TiledMap map;
    public LevelMapRenderer tileMapRenderer;
    public int tilePixelWidth, tilePixelHeight, width, height = 0;
    public List<Entity> entities = new ArrayList<Entity>();
    public Tile tile[][] = new Tile[1024][1024];

    public Comparator<Entity> spriteSorter = (e0, e1) -> {
        if (e1.getSortOrder() < e0.getSortOrder()) return -1;
        if (e1.getSortOrder() > e0.getSortOrder()) return +1;
        return 0;
    };
    private Rectangle rect;

    /**
     *
     * @param level
     * @param entity
     */
    public void add(Level level, Entity entity) {
        entity.removed = false;
        entities.add(entity);
        entity.init(level);
    }

    /**
     * Setups and create A*star
     */
    public Astar createPathFinding(Npc npc) {
        return new Astar(width, height) {
            protected boolean isValid (int x, int y) {
                return getTile(x, y).mayPass && !isBlockingEntitiesAtTile(npc, x, y);
            }
        };
    }

    public void remove(Entity e) {
        entities.remove(e);
    }

    public List<Entity> getEntities() { // optimize this!
        return entities;
    }

    public Comparator<Entity> nearestSorter = new Comparator<Entity>() {
        public int compare(Entity e0, Entity e1) {
            if (e1.getTempFloat() < e0.getTempFloat()) return +1;
            if (e1.getTempFloat() > e0.getTempFloat()) return -1;
            return 0;
        }
    };

    public List<Entity> getNearestEntity(Player player, Rectangle area) {
        List<Entity> result = new ArrayList<Entity>();

        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (e.getWalkBox().overlaps(area) && e.isIntractable()) {
                e.setTempFloat(player.distanceToCenter(e));
                result.add(e);
            }
        }
        Collections.sort(result, nearestSorter);
        return result;
    }

    public List<Entity> getEntities(Rectangle area) {
        List<Entity> result = new ArrayList<Entity>();

        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (e.getWalkBox().overlaps(area)) {
                result.add(e);
            }
        }
        return result;
    }

    public List<Entity> getEntitiesAtTile(int tile_x, int tile_y) {
        Rectangle rect = new Rectangle(tile_x * 16 , tile_y * 16, 16, 16);
        List<Entity> entities =  getEntities(rect);
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (!e.isIntractable()) {
                entities.remove(e);
            }
        }
        return entities;
    }

    public boolean isBlockingEntitiesAtTile(Entity ent, int tile_x, int tile_y) {
        List<Entity> tile_entities = getEntitiesAtTile(tile_x, tile_y);
        for (Entity e : tile_entities) {
            if (e.blocks(ent) && e.isIntractable()) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param entities
     * @param batch
     */
    public void sortAndRender(List<Entity> entities, Batch batch) {
        entities.sort(spriteSorter);
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).draw(batch, 1f);
        }
    }

    public Tile getTile(int x, int y) {
        try {
            return tile[x][y];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return new WaterTile(x, y);
        }
    }

    /**
     * 4 bit bitwise auto tiling
     */
    public int getTileBitwiseIndex(int x, int y) {
        int north_tile, west_tile, east_tile, south_tile;
        if (getTile(x,y+1) instanceof DirtTile) {
            north_tile = 1;
        } else north_tile = 0;

        if (getTile(x-1,y) instanceof DirtTile) {
            west_tile = 1;
        } else west_tile = 0;

        if (getTile(x,y-1) instanceof DirtTile) {
            south_tile = 1;
        } else south_tile = 0;

        if (getTile(x+1, y) instanceof DirtTile) {
            east_tile = 1;
        } else east_tile = 0;

        return north_tile + (2 * west_tile) + (4 * east_tile) + (8 * south_tile);
    }

    /**
     * Used for auto-tiling, just the graphical is changed.
     * @param x
     * @param y
     * @param tilesetIndex
     */
    public void setAutoTile(int x, int y, int tilesetIndex) {
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("ground_top");
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(map.getTileSets().getTile(tilesetIndex));
        layer.setCell(x, y, cell);
    }

    public void setTile(int x, int y, Tile newTile) {
        tile[x][y] =  newTile;
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("ground_top");
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();

        TiledMapTile tile = map.getTileSets().getTile(newTile.getTileSetIndex());
        cell.setTile(tile);
        layer.setCell(x, y, cell);
    }

    public boolean isTileBlocked(int x, int y, Entity e) {
        try {
            return !tile[x][y].mayPass(e);
        } catch (ArrayIndexOutOfBoundsException exc) {
            return true;
        } catch (NullPointerException exc) {
            System.out.println("No tile @ inBoundPos " + x  + "x" + y);
            return true;
        }
    }

    public boolean interact(int x, int y, Entity entity) {
        if (entity instanceof Player) {
            try {
                return tile[x][y].interact((Player) entity, x, y, ((Player) entity).getDirection());
            } catch (ArrayIndexOutOfBoundsException exc) {
                return false;
            } catch (NullPointerException exc) {
                System.out.println("No interaction tile @ inBoundPos " + x  + "x" + y);
                return false;
            }
        }
        return false;
    }

    public Rectangle getTileBounds(int tx, int ty) {
        Rectangle tileRect = new Rectangle(tx * 16, ty * 16, 16, 16);
        return tileRect;
    }

    /**
     *
     * @param level
     * @param i
     */
    public void loadMap(Level level, int i) {
        map = new TmxMapLoader().load("world/world.tmx");

        MapObjects objects = map.getLayers().get("objects").getObjects();
        System.out.println("Total Layers count: " + map.getLayers().getCount());
        System.out.println("Total items found in objects layer: " + objects.getCount());

        for(MapObject object : objects) {

            if (object instanceof TextureMapObject) {
                continue;
            }

            if (object instanceof RectangleMapObject) {
                // Todo: code below..blää.. this has to change!
                String objType = (String) object.getProperties().get("type");
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                if (objType.equals("Door")) { // new generic Type
                    System.out.println("Name of Door: " + object.getName());
                    Door door = new Door(level, new Vector2(rectangle.getX(), rectangle.getY()), rectangle.getWidth(), rectangle.getHeight());
                    door.setTeleportX((int) object.getProperties().get("teleport_x"));
                    door.setTeleportY((int) object.getProperties().get("teleport_y"));
                    add(level, door);
                } else if (objType.equals("AreaLocation")) { // new generic Type
                    System.out.println("Name of Area Location: " + object.getName());
                    add(level, new Location(level, new Vector2(rectangle.getX(), rectangle.getY()), rectangle.getWidth(), rectangle.getHeight(), object.getName(), "todo descr"));
                }
                // System.out.println("type:" + object.getClass() + " " + object.getProperties().get("type") + " " + rectangle);
                // EntityFactory.createEntityFromTileMapObject(Object obj)
            }
        }

        // water
        TiledMapTileLayer waterLayer = (TiledMapTileLayer) map.getLayers().get("water");
        for(int x = 0; x < waterLayer.getWidth();x++) {
            for(int y = 0; y < waterLayer.getHeight();y++) {
                TiledMapTileLayer.Cell waterCell = waterLayer.getCell(x, y);
                if (waterCell != null && waterCell.getTile() != null) {
                    tile[x][y] = new WaterTile(x, y);
                }
            }
        }

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("ground");
        // temp stuff below, must be easy to map TileMap.Tile to (my)Tile
        // TileFactory.createTileFromCell()
        for(int x = 0; x < layer.getWidth();x++) {
            for(int y = 0; y < layer.getHeight();y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);

                // Hmmm this is not good. We treating most Tiles as GrassTiles
                if (cell != null && cell.getTile() != null) {
                    tile[x][y] = new GrassTile(x, y, true);
                }

                // check for tileType
                if (cell != null && cell.getTile().getProperties().containsKey("blocked") && cell.getTile().getProperties().get("blocked").equals(true)) {
                    tile[x][y] = new GrassTile(x, y, false); // masayNot pass grassTile
                }
            }
        }

        // temp override with ground-top
        TiledMapTileLayer layer2 = (TiledMapTileLayer) map.getLayers().get("ground_top");
        // temp stuff below, must be easy to map TileMap.Tile to (my)Tile
        for(int x = 0; x < layer2.getWidth(); x++) {
            for(int y = 0; y < layer2.getHeight();y++) {
                TiledMapTileLayer.Cell cell2 = layer2.getCell(x, y);

                if (cell2 != null && cell2.getTile() != null) {
                    // custom property .. WoodTile is something
                    if (cell2.getTile().getProperties().containsKey("tileType") && (cell2.getTile().getProperties().get("tileType").equals("Wood"))) {
                        tile[x][y] = new WoodTile(x, y);
                    }
                }
                // check for tileType
                if (cell2 != null && cell2.getTile().getProperties().containsKey("blocked") ) {
                    tile[x][y] = new GrassTile(x, y, false); // mayNot pass could be a fence or alike
                }
            }
        }


        // Should we handles blocked tiles like this?
        TiledMapTileLayer blockLayer = (TiledMapTileLayer) map.getLayers().get("blocked");
        for(int x = 0; x < blockLayer.getWidth(); x++) {
            for(int y = 0; y < blockLayer.getHeight();y++) {
                TiledMapTileLayer.Cell cell = blockLayer.getCell(x, y);

                if (cell != null && cell.getTile() != null) {
                    tile[x][y] = new Tile(x, y, false);
                }
            }
        }

        MapProperties properties = map.getProperties();
        width = properties.get("width", Integer.class);
        height = properties.get("height", Integer.class);
        tilePixelWidth = properties.get("tilewidth", Integer.class);
        tilePixelHeight = properties.get("tileheight", Integer.class);
        System.out.println("Map Width: " + width);
        System.out.println("Map Height: " + height);
    }
}