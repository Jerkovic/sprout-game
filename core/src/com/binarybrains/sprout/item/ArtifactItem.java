package com.binarybrains.sprout.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.binarybrains.sprout.entity.Portable;
import com.binarybrains.sprout.item.artifact.Artifact;

public class ArtifactItem extends Item implements Portable {

    private Artifact artifact;
    private boolean carried = false;
    private TextureRegion region;

    public ArtifactItem(Artifact artifact) {
        this.artifact = artifact;
    }

    public String getName() {
        return artifact.getName();
    }

    public boolean matches(Item item) {
        if (item instanceof ArtifactItem) {
            ArtifactItem other = (ArtifactItem) item;
            if (!other.artifact.getClass().equals(artifact.getClass())) return false;
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return artifact.getDescription();
    }

    @Override
    public String getRegionId() {
        return artifact.name.replaceAll(" ", "_");

    }

    @Override
    public void setCarried() {
        carried = true;
    }

    @Override
    public void deleteCarried() {
        carried = false;
    }


}
