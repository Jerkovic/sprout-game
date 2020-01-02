package com.binarybrains.sprout.level;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.house.Cottage;
import com.binarybrains.sprout.entity.house.Tower;
import com.binarybrains.sprout.entity.tree.Bush;
import com.binarybrains.sprout.entity.tree.SmallTree;
import com.binarybrains.sprout.entity.tree.Tree;
import com.binarybrains.sprout.level.pathfind.Astar;
import com.binarybrains.sprout.level.tile.*;
import com.binarybrains.sprout.locations.Bed;
import com.binarybrains.sprout.locations.Bridge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class LevelEngine {

    public TiledMap map;
    public OrthogonalTiledMapRenderer tileMapRenderer;
    public int tilePixelWidth, tilePixelHeight, width, height = 0;
    public List<Entity> entities = new ArrayList<Entity>();
    public Astar astar; // should this be here?
    public Tile tile[][] = new Tile[256][128];

    public Comparator<Entity> spriteSorter = new Comparator<Entity>() {
        public int compare(Entity e0, Entity e1) {
            if (e1.getSortOrder() < e0.getSortOrder()) return -1;
            if (e1.getSortOrder() > e0.getSortOrder()) return +1;
            return 0;
        }
    };

    public void add(Level level, Entity entity) {
        entity.removed = false;
        entities.add(entity);
        entity.init(level);
    }

    /**
     * Setups and create A*star
     */
    public void setupPathFinding() {
        // should be the size of the map
        astar = new Astar(256, 128) {
            protected boolean isValid (int x, int y) {
                return getTile(x, y).mayPass && getEntitiesAtTile(x, y).size() == 0;
            }
        };
    }

    public IntArray getPath(int startX, int startY, int targetX, int targetY) {
        return astar.getPath(startX, startY, targetX, targetY);
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
            if (e.getWalkBox().overlaps(area)) {
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
        return getEntities(rect);
    }

    public boolean isBlockingEntitiesAtTile(Entity ent, int tile_x, int tile_y) {
        List<Entity> tile_entities = getEntitiesAtTile(tile_x, tile_y);
        for (int i = 0; i < tile_entities.size(); i++) {
            Entity e = tile_entities.get(i);
            if (e.blocks(ent)) {
                return true;
            }
        }
        return false;
    }

    public void sortAndRender(List<Entity> entities, Batch batch) {
        Collections.sort(entities, spriteSorter);
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

    public void loadMap(Level level, int i) {
        map = new TmxMapLoader().load("levels/sdv_level" + i + ".tmx");

        MapObjects objects = map.getLayers().get("objects").getObjects();
        System.out.println("# of objs found in objects layer: " + objects.getCount());

        for(MapObject object : objects) {

            if (object instanceof TextureMapObject) {
                continue;
            }

            if (object instanceof RectangleMapObject) {

                // Todo: code below..blää.. this has to change!
                String objType = (String) object.getProperties().get("type");
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                if (objType.equals("bigtree")) {
                    add(level, new Tree(level, new Vector2(rectangle.getX(), rectangle.getY()), rectangle.getWidth(), rectangle.getHeight()));
                } else if (objType.equals("cottage")) {
                    Cottage cottage = new Cottage(level, new Vector2(rectangle.getX(), rectangle.getY()), rectangle.getWidth(), rectangle.getHeight());
                    cottage.setDoorTilePos((Integer) object.getProperties().get("doorTileX"));
                    add(level, cottage);
                } else if (objType.equals("tower")) {
                    Tower tower = new Tower(level, new Vector2(rectangle.getX(), rectangle.getY()), rectangle.getWidth(), rectangle.getHeight());
                    tower.setDoorTilePos((Integer) object.getProperties().get("doorTileX"));
                    add(level, tower);
                } else if (objType.equals("BridgeTrigger")) { // location triggers
                    String desc = object.getProperties().get("description").toString();
                    //String name = object.getProperties().get("Name").toString();
                    add(level, new Bridge(level, new Vector2(rectangle.getX(), rectangle.getY()), rectangle.getWidth(), rectangle.getHeight(),"Bridge trigger", desc));
                } else if (objType.equals("BedTrigger")) { // location triggers
                    String desc = object.getProperties().get("description").toString();
                    //String name = object.getProperties().get("Name").toString();
                    add(level, new Bed(level, new Vector2(rectangle.getX(), rectangle.getY()), rectangle.getWidth(), rectangle.getHeight(),"Go to bed", desc));
                } else if (objType.equals("SmallTree")) {
                    add(level, new SmallTree(level, new Vector2(rectangle.getX(), rectangle.getY()), rectangle.getWidth(), rectangle.getHeight()));
                } else if (objType.equals("Bush")) {
                    add(level, new Bush(level, new Vector2(rectangle.getX(), rectangle.getY()), rectangle.getWidth(), rectangle.getHeight()));
                }
                // System.out.println("type:" + object.getClass() + " " + object.getProperties().get("type") + " " + rectangle);
                // EntityFactory.createEntityFromTileMapObject(Object obj)
            }
        }

        // water
        TiledMapTileLayer waterLayer = (TiledMapTileLayer) map.getLayers().get("water");
        // TileFactory.createTileFromCell()
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
                    // custom property
                    if (cell.getTile().getProperties().containsKey("tileType") && (cell.getTile().getProperties().get("tileType").equals("teleporter"))) {
                        tile[x][y] = new TeleportTile(x, y);
                    }
                    if (cell.getTile().getProperties().containsKey("tileType") && (cell.getTile().getProperties().get("tileType").equals("Wood"))) {
                        tile[x][y] = new WoodTile(x, y);
                    }
                }

                // check for tileType
                if (cell != null && cell.getTile().getProperties().containsKey("blocked") && cell.getTile().getProperties().get("blocked").equals(true)) {
                    tile[x][y] = new GrassTile(x, y, false); // mayNot pass grassTile
                }

            }
        }

        // temp override with ground-top
        TiledMapTileLayer layer2 = (TiledMapTileLayer) map.getLayers().get("ground_top");
        // temp stuff below, must be easy to map TileMap.Tile to (my)Tile
        for(int x = 0; x < layer2.getWidth();x++) {
            for(int y = 0; y < layer2.getHeight();y++) {
                TiledMapTileLayer.Cell cell2 = layer2.getCell(x, y);

                if (cell2 != null && cell2.getTile() != null) {
                    // custom property .. wooden stuff

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

        MapProperties properties = map.getProperties();
        width = properties.get("width", Integer.class);
        height = properties.get("height", Integer.class);
        tilePixelWidth = properties.get("tilewidth", Integer.class);
        tilePixelHeight = properties.get("tileheight", Integer.class);
    }


}
