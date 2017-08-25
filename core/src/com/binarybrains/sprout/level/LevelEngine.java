package com.binarybrains.sprout.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.house.Cottage;
import com.binarybrains.sprout.entity.tree.SmallTree;
import com.binarybrains.sprout.entity.tree.Tree;
import com.binarybrains.sprout.level.pathfind.Astar;
import com.binarybrains.sprout.level.tile.*;
import com.binarybrains.sprout.locations.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LevelEngine {

    public TiledMap map;
    public OrthogonalTiledMapRenderer tileMapRenderer;
    public int tilePixelWidth, tilePixelHeight, width, height = 0;
    public List<Entity> entities = new ArrayList<Entity>();
    public Astar astar; // should this be here?
    public Tile tile[][] = new Tile[256][128];

    public Comparator<Entity> spriteSorter = new Comparator<Entity>() {
        public int compare(Entity e0, Entity e1) {
            if (e1.getY() < e0.getY()) return -1;
            if (e1.getY() > e0.getY()) return +1;
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
                return getTile(x,y).mayPass && getEntitiesAtTile(x, y).size() == 0;
            }
        };
    }

    // should this take Entity?
    public IntArray getPath(int startX, int startY, int targetX, int targetY) {
        return astar.getPath(startX, startY, targetX, targetY);
    }

    public void remove(Entity e) {
        entities.remove(e);
    }

    public List<Entity> getEntities() { // optimize this!
        return entities;
    }

    // TODO: should probably need to another; get entities by a certain type/class
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
            return new WaterTile();
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

        cell.setTile(map.getTileSets().getTile(newTile.getTileSetIndex()));
        layer.setCell(x, y, cell);
    }

    public boolean isTileBlocked(int x, int y, Entity e) {
        try {
            if (x < 1) return true;
            if (y < 1) return true;
            return !tile[x][y].mayPass(e);
        } catch (ArrayIndexOutOfBoundsException exc) {
            return true;
        }

    }

    public void interact(int x, int y, Entity entity) {
        if (entity instanceof Player) {
            tile[x][y].interact((Player) entity, x, y, ((Player) entity).getDirection());
        }
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

                String objType = (String) object.getProperties().get("type");
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                if (objType.equals("bigtree")) {
                    add(level, new Tree(level, new Vector2(rectangle.getX(), rectangle.getY()), rectangle.getWidth(), rectangle.getHeight()));
                } else if (objType.equals("cottage")) {
                    // Gdx.app.log("", "Instance info" + object.getProperties().get("description"));
                    add(level, new Cottage(level, new Vector2(rectangle.getX(), rectangle.getY()), rectangle.getWidth(), rectangle.getHeight()));
                } else if (objType.equals("Trigger")) {
                    System.out.println(object.getProperties());
                    String desc = object.getProperties().get("description").toString();
                    add(level, new Location(level, new Vector2(rectangle.getX(), rectangle.getY()), rectangle.getWidth(), rectangle.getHeight(),"Trigger", desc));
                } else if (objType.equals("SmallTree")) {

                    add(level, new SmallTree(level, new Vector2(rectangle.getX(), rectangle.getY()), rectangle.getWidth(), rectangle.getHeight()));
                }
                System.out.println("type:" + object.getClass() + " " + object.getProperties().get("type") + " " + rectangle);
                // EntityFactory.createEntityFromTileMapObject(Object obj)
            }
        }

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("ground");
        // temp stuff below, must be easy to map TileMap.Tile to (my)Tile
        for(int x = 0; x < layer.getWidth();x++) {
            for(int y = 0; y < layer.getHeight();y++) {
                tile[x][y] = new GrassTile();
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell != null && cell.getTile().getProperties().containsKey("blocked") ) {
                    tile[x][y] = new WaterTile();
                }
            }
        }

        // temp override with ground-top
        TiledMapTileLayer layer2 = (TiledMapTileLayer) map.getLayers().get("ground_top");
        // temp stuff below, must be easy to map TileMap.Tile to (my)Tile
        for(int x = 0; x < layer2.getWidth();x++) {
            for(int y = 0; y < layer2.getHeight();y++) {
                TiledMapTileLayer.Cell cell2 = layer2.getCell(x, y);
                if (cell2 != null && cell2.getTile().getProperties().containsKey("blocked") ) {
                    tile[x][y] = new GrassTile(false); // just test

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
