package com.binarybrains.sprout.util.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class YamlLoader extends SynchronousAssetLoader<YamlFile, YamlLoader.YamlLoaderParameters> {

    public YamlLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public YamlFile load(AssetManager assetManager, String fileName, FileHandle file, YamlLoaderParameters parameter) {
        return null;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, YamlLoaderParameters parameter) {
        return null;
    }

    static public class YamlLoaderParameters extends AssetLoaderParameters<YamlFile> {

        public YamlLoaderParameters() {
        }
    }
}
