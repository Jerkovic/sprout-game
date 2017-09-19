package com.binarybrains.sprout.item;

import com.binarybrains.sprout.item.artifact.Artifact;

public class ArtifactItem extends Item {

    private Artifact artifact;

    public ArtifactItem(Artifact artifact) {
        this.artifact = artifact;
        // category = Artifact
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
}
