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
import com.binarybrains.sprout.level.tile.GrassTile;
import com.binarybrains.sprout.level.tile.Tile;
import com.binarybrains.sprout.level.tile.WaterTile;

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
        Rectangle rect = new Rectangle(tile_x * 16 ,tile_y * 16, 16, 16);
        return getEntities(rect);
    }


    public void sortAndRender(List<Entity> entities, Batch batch) {
        Collections.sort(entities, spriteSorter);
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).draw(batch, 1f);
        }
    }

    public Tile getTile(int x, int y) {
        // todo add try catch OutofBounds
        return tile[x][y];
    }

    public void setTile(int x, int y, Tile newTile) {
        tile[x][y] = newTile;
    }

    public boolean isTileBlocked(int x, int y, Entity e) {
        if (x < 1) return true;
        if (y < 1) return true;
        return !tile[x][y].mayPass(e);
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
        map = new TmxMapLoader().load("levels/level" + i + ".tmx");

        MapObjects objects = map.getLayers().get("objects").getObjects();
        System.out.println("# of objs found in objects layer: " + objects.getCount());

        /*
        MapObjects objects2 = map.getLayers().get("test").getObjects();

        System.out.println("# of objs found in TEST layer: " + objects2.getCount());

        for(MapObject object : objects2) {
            System.out.println(object);
            if (object instanceof TiledMapTileMapObject) {
                System.out.println(((TiledMapTileMapObject) object).getTile().getId());
            }
        }
        */
        for(MapObject object : objects) {

            if (object instanceof TextureMapObject) {
                continue;
            }

            if (object instanceof RectangleMapObject) {

                String objType = (String) object.getProperties().get("type");
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                if (objType.equals("bigtree")) {
                    add(level, new Tree(level, new Vector2(rectangle.getX(), rectangle.getY()), rectangle.getWidth(), rectangle.getHeight()));
                } else if (objType.equals("house")) {
                    Gdx.app.log("", "Instance info" + object.getProperties().get("description"));
                    add(level, new Cottage(level, new Vector2(rectangle.getX(), rectangle.getY()), rectangle.getWidth(), rectangle.getHeight()));
                } else if (objType.equals("someitem")) {
                    // something
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
                if (cell.getTile().getProperties().containsKey("blocked") ) {
                    tile[x][y] = new WaterTile();
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
